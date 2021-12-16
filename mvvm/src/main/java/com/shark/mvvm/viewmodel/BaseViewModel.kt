package com.shark.mvvm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.shark.mvvm.datasource.DataSource
import com.shark.mvvm.exception.BaseException
import com.shark.mvvm.retrofit.model.BaseRequestModel
import com.shark.mvvm.service.Service
import com.shark.mvvm.spread.TAG
import io.reactivex.Observable


open class BaseViewModel(application: Application) : AndroidViewModel(application),
    IViewModelAction {
    //这样可以让Activity监听到 Live数据的变化
    var lifecycleOwner: LifecycleOwner? = null
    override var actionLiveData: MutableLiveData<BaseActionEvent>? = null

    val dataSource: DataSource = DataSource(this)

    /**
     * 发送请求
     * @param observable Observable<M>
     * @param isDismiss Boolean
     * @param isLoad Boolean
     * @param failCallback Function1<[@kotlin.ParameterName] BaseException?, Unit>?
     * @param successCallback Function1<[@kotlin.ParameterName] T, Unit>?
     */
    open fun <T, M : BaseRequestModel<T>> call(
        observable: Observable<M>,
        isDismiss: Boolean = true,
        isLoad: Boolean = true,
        failCallback: ((e: BaseException?) -> Unit)? = null,
        successCallback: ((result: T) -> Unit)? = null
    ) {
        dataSource.execute(observable, isDismiss, isLoad, failCallback, successCallback)
    }

    init {
        actionLiveData = MutableLiveData()

        this::class.java.declaredFields.forEach {
            it.isAccessible = true
//            val annotation = it.getAnnotation(SharkViewModel::class.java)
            if (it.isAnnotationPresent(Service::class.java)) {
                val service = dataSource.getService(it.type as Class)
                Log.i(TAG, "service: $service")
                it.set(this, service)
            }
        }
    }

    override fun startLoading() {
        startLoading(null);
    }

    override fun startLoading(message: String?) {
        val baseActionEvent = BaseActionEvent(BaseActionEvent.SHOW_LOADING_DIALOG)
        baseActionEvent.message = message
        actionLiveData!!.value = baseActionEvent
    }

    override fun dismissLoading() {
        actionLiveData?.value = BaseActionEvent(BaseActionEvent.DISMISS_LOADING_DIALOG);
    }

    override fun showToast(message: String?) {
        val baseActionEvent = BaseActionEvent(BaseActionEvent.SHOW_TOAST)
        baseActionEvent.message = message
        actionLiveData!!.value = baseActionEvent
    }

    override fun logicErrorShowToast(message: String?) {
        val baseActionEvent = BaseActionEvent(BaseActionEvent.LOGIC_ERROR)
        baseActionEvent.message = message
        actionLiveData!!.value = baseActionEvent
    }

    override fun finish() {
        actionLiveData?.value = BaseActionEvent(BaseActionEvent.FINISH)
    }

    override fun finishWithResultOk() {
        actionLiveData?.value = BaseActionEvent(BaseActionEvent.FINISH_WITH_RESULT_OK)
    }

}