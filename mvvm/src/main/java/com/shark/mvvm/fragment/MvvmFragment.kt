package com.shark.mvvm.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.shark.mvvm.activity.BaseActivity
import com.shark.mvvm.event.CleanModel
import com.shark.mvvm.fragment.BaseFragment
import com.shark.mvvm.viewmodel.BaseActionEvent
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.mvvm.viewmodel.SharkViewModel
import com.zyao89.view.zloading.ZLoadingDialog
import com.zyao89.view.zloading.Z_TYPE
import java.lang.RuntimeException

open class MvvmFragment : BaseFragment() {

    //初始化加载框
    private var loadingDialog: ZLoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val result = super.onCreateView(inflater, container, savedInstanceState)

        //初始化加载框
        activity?.let {
            loadingDialog = ZLoadingDialog(it).apply {
                setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE) //设置类型
                setLoadingColor(Color.WHITE) //颜色
                setHintTextSize(16f) // 设置字体大小 dp
                setCanceledOnTouchOutside(false)
                setHintTextColor(Color.WHITE) // 设置字体颜色
                setDialogBackgroundColor(Color.parseColor("#00000000")) // 设置背景色，默认白色
            }
        }

        initViewModelEvent()

        return result
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
                it.lifecycleOwner = this
                it.actionLiveData!!.observe(
                    viewLifecycleOwner,
                    { baseActionEvent: BaseActionEvent ->
                        when (baseActionEvent.action) {
                            BaseActionEvent.SHOW_LOADING_DIALOG -> {
                                startLoading(baseActionEvent.message)
                            }
                            BaseActionEvent.LOGIC_ERROR -> {
                                val baseActivity = activity as BaseActivity
                                baseActivity.cleanEdit()
                                showToast(baseActionEvent.message)
                            }
                            BaseActionEvent.DISMISS_LOADING_DIALOG -> {
                                val baseActivity = activity as BaseActivity
                                if (baseActivity.cleanEdit == CleanModel.ALWAYS) {
                                    baseActivity.cleanEdit()
                                }
                                dismissLoading()
                            }
                            BaseActionEvent.SHOW_TOAST -> showToast(baseActionEvent.message)
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
        loadingDialog?.apply {
            setHintText(message)
            show()
        }
    }

    protected open fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    protected open fun showToast(message: String?) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        val baseActivity = activity as BaseActivity
        message?.let { baseActivity.alertDialog(it, time = 5) }

    }
}