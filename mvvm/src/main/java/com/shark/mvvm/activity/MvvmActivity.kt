package com.shark.mvvm.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.shark.mvvm.event.CleanModel
import com.shark.mvvm.viewmodel.BaseActionEvent
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.mvvm.viewmodel.SharkViewModel
import com.zyao89.view.zloading.ZLoadingDialog
import com.zyao89.view.zloading.Z_TYPE
import java.lang.RuntimeException

/**
 * ViewModel + LiveData + Retrofit + RxJava 整合Activity
 */
abstract class MvvmActivity : BaseActivity() {
    //初始化加载框
    private val loadingDialog: ZLoadingDialog = ZLoadingDialog(this).apply {
        setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE) //设置类型
        setLoadingColor(Color.WHITE) //颜色
        setHintTextSize(16f) // 设置字体大小 dp
        setCanceledOnTouchOutside(false)
        setHintTextColor(Color.WHITE) // 设置字体颜色
        setDialogBackgroundColor(Color.parseColor("#00000000")) // 设置背景色，默认白色
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelEvent()
    }

    /**
     * 初始化ViewModel事件
     */
    private fun initViewModelEvent() {
        val modelList: MutableList<ViewModel> = ArrayList()
        //获取全部@SharkViewModel 并且在此判断是否成注入
        this::class.java.declaredFields.forEach {
            if (it.isAnnotationPresent(SharkViewModel::class.java)) {
                it.isAccessible = true
                val fieldObject =
                    it.get(this) ?: throw RuntimeException("$this injectionViewModel $it fail")
                modelList.add(fieldObject as ViewModel)
            }
        }

        observeEvent(modelList)
    }

    /**
     * 最后把所以有关的ViewModel拿到此处 进行事件监听
     * @param viewModelList List<ViewModel>
     */
    private fun observeEvent(viewModelList: List<ViewModel>) {
        for (viewModel in viewModelList) {
            (viewModel as? BaseViewModel)?.let {
                //让我们的BaseActivity 对 BaseViewModel的actionLiveData进行监听
                // 数据发生改变的时候进行视图的变化
                it.lifecycleOwner = this@MvvmActivity
                it.actionLiveData!!.observe(this@MvvmActivity, { baseActionEvent: BaseActionEvent ->
                    when (baseActionEvent.action) {
                        BaseActionEvent.SHOW_LOADING_DIALOG -> {
                            startLoading(baseActionEvent.message)
                        }
                        BaseActionEvent.LOGIC_ERROR -> {
                            cleanEdit()
                            dismissLoading()
                        }
                        BaseActionEvent.DISMISS_LOADING_DIALOG -> {
                            if (cleanEdit == CleanModel.ALWAYS) {
                                cleanEdit()
                            }
                            dismissLoading()
                        }
                        BaseActionEvent.SHOW_TOAST -> showToast(baseActionEvent.message)
                        BaseActionEvent.FINISH -> finish()
                        BaseActionEvent.FINISH_WITH_RESULT_OK -> {
                            setResult(RESULT_OK)
                            finish()
                        }
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

    protected open fun startLoading() {
        startLoading(null)
    }

    protected open fun startLoading(message: String?) {
        loadingDialog.apply {
            setHintText(message)
            show()
        }
    }

    protected open fun dismissLoading() {
        loadingDialog.dismiss()
    }

    protected open fun showToast(message: String?, titleText: String = "提示") {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        message?.let { alertDialog(titleText, it) }
    }

    protected open fun finishWithResultOk() {
        setResult(RESULT_OK)
        finish()
    }
}