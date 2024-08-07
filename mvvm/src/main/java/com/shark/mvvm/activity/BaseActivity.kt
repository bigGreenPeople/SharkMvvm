package com.shark.mvvm.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shark.mvvm.R
import com.shark.mvvm.config.ScanConfig
import com.shark.mvvm.event.CleanModel
import com.shark.mvvm.utils.Hide
import com.shark.mvvm.event.ScanEvent
import com.shark.mvvm.event.ScanEventInfo
import com.shark.mvvm.eventbus.EventBusEnabledHandler
import com.shark.mvvm.utils.StringUtils
import com.shark.mvvm.utils.XToastUtils
import com.shark.mvvm.utils.schedule
import com.shark.mvvm.view.TitleListener
import com.shark.mvvm.viewmodel.SharkViewModel
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import org.greenrobot.eventbus.EventBus
import java.lang.RuntimeException

import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog


/**
 * 此类用于处理Activity的一些公共处理
 */

open class BaseActivity : AppCompatActivity(), TitleListener {
    val TAG = "SharkChilli"
    override var sharkActivity: SharkActivity? = null
    var viewDataBinding: ViewDataBinding? = null

    //指定布局文件属性
    private var layoutId: Int? = null

    //所有扫描注解的方法都存在这个list中
    private val listScanEventInfo: MutableList<ScanEventInfo> = mutableListOf()

    init {
        //获取SharkActivity注解类
        this::class.annotations.find { it is SharkActivity }
            ?.let { sharkActivity = it as SharkActivity }
        //解析Activity扫描方法
        setScanEventListener(this)
    }

    /**
     * 因为在 Module lib 中无法在编译前确定R文件的内容 所以我们可能不能使用@SharkActivity注解
     * 这个时候可以手动初始化sharkActivity属性
     */
    fun initSharkActivity(layoutId: Int) {
        this.layoutId = layoutId
    }


    /**
     * 设置监听
     */
    fun setScanEventListener(listener: Any) {
        for (declaredFunction in listener::class.java.declaredMethods) {
            val scanEvent = declaredFunction.annotations.find { it is ScanEvent }
            scanEvent?.let {
                Log.i(TAG, "setScanEventListener: 添加方法 ${declaredFunction.name}")
                listScanEventInfo?.add(
                    ScanEventInfo(scanEvent as ScanEvent, declaredFunction, listener)
                )
            }
        }
    }

    /**
     * 获得ViewModel 改方法只能调用一次
     * @param clazz 获得的ViewModel类
     */
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            clazz
        )
    }

    /**
     * 初始化公共操作
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注入ViewModel值
        ActivityManager.addActivity(this)
        injectionViewModel()

        sharkActivity?.let {
            viewDataBinding = initDataBinding(it.layoutId)
        }

        if (sharkActivity == null) {
            viewDataBinding = initDataBinding(layoutId!!)
        }

        viewDataBinding?.lifecycleOwner = this

        initView()
        eventBusEnabled()


        //设置键盘出现移动布局
        keyBoardMove()
    }

    /**
     * 注入ViewModel
     */
    private fun injectionViewModel() {
        // 扫描出本类所有@SharkViewModel 设置值
        this::class.java.declaredFields.forEach {
            it.isAccessible = true
//            val annotation = it.getAnnotation(SharkViewModel::class.java)
            if (it.isAnnotationPresent(SharkViewModel::class.java)) {
                val viewModel = getViewModel(it.type as Class<ViewModel>)
                Log.i(TAG, "injectionViewModel: $viewModel")
                it.set(this, viewModel)
            }
        }
    }

    /**
     * 设置键盘出现移动布局
     */
    private fun keyBoardMove() {
        val keyBoardMove =
            this::class.annotations.find { it is KeyBoardMove }?.let { it as KeyBoardMove }
        //如果标注了KeyBoardMove注解设置移动事件
        keyBoardMove?.let {
            //弹出软键盘时页面显示控制
            val view = findViewById<View>(it.moveId)
            val moveToView = findViewById<View>(it.moveToId)
            view?.viewTreeObserver?.addOnGlobalLayoutListener {
                val rect = Rect()
                //getWindowVisibleDisplayFrame 获取当前窗口可视区域大小
                window.decorView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = window.decorView.height
                //键盘弹出时，可视区域大小改变，屏幕高度 - 窗口可视区域高度 = 键盘弹出高度
                val softHeight = screenHeight - rect.bottom

                /**
                 * 上移的距离 = 键盘的高度 - 按钮距离屏幕底部的高度(如果手机高度很大，上移的距离会是负数，界面将不会上移)
                 * 按钮距离屏幕底部的高度是用屏幕高度 - 按钮底部距离父布局顶部的高度
                 * 注意这里 btn.getBottom() 是按钮底部距离父布局顶部的高度，这里也就是距离最外层布局顶部高度
                 */
                val scrollDistance: Int = softHeight - (screenHeight - moveToView.bottom)
                if (scrollDistance > 0) {
                    //具体移动距离可自行调整
                    view.scrollTo(0, scrollDistance + 5)
                } else {
                    //键盘隐藏，页面复位
                    view?.scrollTo(0, 0)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        eventBusClosed()
    }


    /**
     * 初始DataBinding
     */
    private fun initDataBinding(layoutId: Int): ViewDataBinding {
        //寻找定义的DataBinding属性如果不存在 抛出异常
        this::class.java.declaredFields.forEach {
            it.isAccessible = true
            if (ViewDataBinding::class.java.isAssignableFrom(it.type)) {

                if (layoutId == 0) {
                    //获取R.layout.id
                    val idName = StringUtils.underscoreName(
                        it.type.simpleName.replace("Binding", "").replace("Impl", "")
                    )
                    val genLayoutId: Int = resources.getIdentifier(idName, "layout", packageName)

                    if (genLayoutId == 0) throw RuntimeException("R.layout.$idName does not exist!!")

                    it.set(this, DataBindingUtil.setContentView(this, genLayoutId))

                } else {
                    it.set(this, DataBindingUtil.setContentView(this, layoutId))
                }

                //设置页面
                val viewDataBinding: ViewDataBinding = it.get(this) as ViewDataBinding
                return viewDataBinding
            }
        }

        throw RuntimeException("you must declare ViewDataBinding Field in you Activity!!")
    }

    /**
     * 关于界面初始化等操作建议都在此处做
     */
    open fun initView() {
    }

    /**
     * 页面跳转
     */
    fun jumpActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    /**
     * 返回
     */
    override fun back(view: View) {
        Log.i(TAG, "finish ${this::class}")
        finish()
    }

    /**
     * 主要处理分发扫描事件
     *
     * @param event
     * @return
     */
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {

        for (scanEventInfo in listScanEventInfo) {
            if (event!!.action == scanEventInfo.scanEvent.action) {
//                if (event!!.keyCode == 523) {
//                    Log.i(TAG, "dispatchKeyEvent: ${event.keyCode}:${event.action}")
//                }
                //都不符合所有扫描键提前退出
                if (event.keyCode != scanEventInfo.scanEvent.code
                    && !ScanConfig.keySet.contains(event.keyCode)
                ) continue

                //获取当前焦点的文本 如果没有则返回空字符串
                val focusView = window.decorView.findFocus() as? TextView

                // 指定了扫描文本框 如果与对应焦点文本框不一致也退出
                if (scanEventInfo.scanEvent.id != 0 && focusView?.id != scanEventInfo.scanEvent.id) continue

                //检测是否是可读文本控件
                val focusViewText = focusView?.text.toString()
                //检测文本是否为空
                if (!scanEventInfo.scanEvent.scanNull) {
                    if (TextUtils.isEmpty(focusViewText) || focusViewText.trim { it <= ' ' }
                            .isEmpty()) continue
                }

                if ((scanEventInfo.scanEvent.code != 0 && event.keyCode == scanEventInfo.scanEvent.code)
                    || (scanEventInfo.scanEvent.code == 0 && ScanConfig.keySet.contains(event.keyCode))
                ) {
                    //判断是否有更匹配id的event 方法
                    val findScanEventInfo =
                        listScanEventInfo.find {
                            it.scanEvent.id == focusView?.id
                                    && it.scanEvent.action == event.action
                                    && (it.scanEvent.code == 0 || it.scanEvent.code == event.keyCode)
                        }
                    //最匹配的不是当前的就先跳过 下一轮最匹配的才执行
                    if (findScanEventInfo != null && findScanEventInfo != scanEventInfo) continue

                    //TODO 这里要做一个锁 如果锁是锁住的状态就不做处理 如果锁不是锁住的状态就做处理 (由于时间问题我们暂时使用Volatile变量来处理这个问题)
                    //防止\n两次扫描事件触发 也防止快速扫描多次
                    if (preEvent != null && event.action == preEvent?.action && event.eventTime - preEvent!!.eventTime < 500) {
                        continue
                    }
                    preEvent = event

                    //判断此次扫描事件是否要时清除编辑框
                    if (scanEventInfo.scanEvent.clean != CleanModel.NEVER) {
                        cleanEdit = scanEventInfo.scanEvent.clean
                        cleanId = scanEventInfo.scanEvent.id
                    } else {
                        cleanEdit = scanEventInfo.scanEvent.clean
                        cleanId = null
                    }
                    //指定了code 执行此处
                    scanEventInfo.scanFun.invoke(scanEventInfo.objectThis, focusViewText)
                    break
                }
            }
        }


        if (ScanConfig.closeSystemKeySet.contains(event?.keyCode)) return false

        return super.dispatchKeyEvent(event)
    }

    //上一个KeyEvent
    @Volatile
    public var preEvent: KeyEvent? = null


    //请求清除编辑框标识
    @Volatile
    public var cleanEdit: CleanModel = CleanModel.NEVER

    @Volatile
    private var cleanId: Int? = null

    /**
     * 清除编辑框
     */
    fun cleanEdit() {
        if (cleanEdit == CleanModel.NEVER) return

        cleanEdit = CleanModel.NEVER

        //判断现在获取焦点的编辑框是否还是当前编辑框

        val cleanEditText = findViewById<EditText>(cleanId!!)
        cleanEditText?.setText("")
    }

    /**
     * 全选操作的编辑框
     */
    fun selectEdit() {
        cleanEdit = CleanModel.NEVER

        val editText = findViewById<EditText>(cleanId!!)
        editText.selectAll()
    }

    /**
     * 点击其他控件时关闭键盘
     * @param ev MotionEvent
     * @return Boolean
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            val v = currentFocus
            if (Hide.isShouldHideInput(v, ev)) {
                Hide.hideSoftInput(v!!.windowToken, this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 显示简单弹框
     * @param titleText String
     * @param content String
     * @param confirm String
     * @param isSelected Boolean 弹框确认后是否全选
     */
    fun alertDialog(
        titleText: String = "提示",
        content: String = "",
        confirm: String = "好的",
        isSelected: Boolean = true,
        time: Long = 0L
    ) {
        MaterialDialog.Builder(this).iconRes(R.drawable.icon_tip).title(titleText)
            .content(content)
            .showListener { dialog ->
                if (dialog is MaterialDialog) {
                    dialog as MaterialDialog
                    var timeTmp = time
                    if (timeTmp != 0L) {
                        schedule { timer ->
                            runOnUiThread {
                                dialog.setTitle("$titleText (${timeTmp}s)")
                            }
                            if (timeTmp == 0L) {
                                runOnUiThread {
                                    timer.cancel()
                                    dialog.dismiss()
                                }

                            }
                            timeTmp -= 1
                        }
                    }


                }
            }
            .positiveText(confirm).onPositive { _, _ ->
                val focusView = window.decorView.findFocus() as? EditText
                if (isSelected) focusView?.selectAll()
            }.show()

    }


    fun info(
        message: String, isSelected: Boolean = false
    ) {
        XToastUtils.info(message)
        if (isSelected) {
            val focusView = window.decorView.findFocus() as? EditText
            focusView?.selectAll()
        }
    }

    fun error(
        message: String, isSelected: Boolean = true
    ) {
        XToastUtils.error(message)
        val focusView = window.decorView.findFocus() as? EditText
        if (isSelected) focusView?.selectAll()
    }


    /**
     * 弹出警告
     * @param titleText String
     * @param content String
     * @param confirm String
     * @param cancel String
     * @param cancelClick Function0<Unit>?
     * @param okClick Function0<Unit>?
     */
    fun alertWarning(
        titleText: String = "警告",
        content: String = "",
        confirm: String = "确定",
        cancel: String = "取消",
        isSelected: Boolean = true,
        cancelClick: (() -> Unit)? = null,
        okClick: (() -> Unit)? = null
    ) {
        MaterialDialog.Builder(this).iconRes(R.drawable.icon_warning).title(titleText)
            .content(content).positiveText(confirm).negativeText(cancel)
            .onNegative { dialog, which ->
                cancelClick?.invoke()
            }.onPositive { dialog, which ->
                okClick?.invoke()
                val focusView = window.decorView.findFocus() as? EditText
                if (isSelected) focusView?.selectAll()
            }.show()
    }

    /**
     * 处理开启 eventBus
     */
    @Deprecated("Please Use Rxjava")
    fun eventBusEnabled() {
        if (EventBusEnabledHandler.isEventBusEnabled(this)) {
            Log.i(TAG, "开启EventBus: ${this.javaClass}")
            EventBus.getDefault().register(this)
        }
    }

    /**
     * 处理关闭 eventBus
     */
    @Deprecated("Please Use Rxjava")
    fun eventBusClosed() {
        if (EventBusEnabledHandler.isEventBusEnabled(this)) {
            Log.i(TAG, "关闭EventBus: ${this.javaClass}")

            EventBus.getDefault().unregister(this)
        }
    }
}