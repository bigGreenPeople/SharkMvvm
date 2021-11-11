package com.shark.mvvm.retrofit.subscriber

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.shark.mvvm.viewmodel.BaseViewModel
import io.reactivex.observers.DisposableObserver
import com.shark.mvvm.config.HttpCode
import com.shark.mvvm.exception.BaseException


import com.shark.mvvm.retrofit.callback.RequestCallback
import com.shark.mvvm.retrofit.callback.RequestMultiplyCallback


/**
 * 订阅者 或者叫 监听者 (一次请求会创建一个BaseRemoteSubscriber)
 * @param T
 */
class BaseRemoteSubscriber<T>(
    private val baseViewModel: BaseViewModel,
    var requestCallback: RequestCallback<T>? = null
) : DisposableObserver<T>() {
    /**
     * 查看是否执行了任一onNext或者onError
     */
    var isExecute: Boolean = false

    //成功执行设置的回调
    override fun onNext(t: T) {
        isExecute = true
        requestCallback?.onSuccess(t)
    }

    /**
     * 数据过滤抛出异常时会来到这
     * 这里如果设置的回调为 RequestMultiplyCallback类型就执行 onFail的逻辑
     * @param e Throwable
     */
    override fun onError(e: Throwable) {
        Log.i("BaseSubscriber", "onError: ", e)
        isExecute = true

        if (requestCallback is RequestMultiplyCallback) {
            val callback = requestCallback as RequestMultiplyCallback
            if (e is BaseException) {
                callback.onFail(e)
            } else {
                callback.onFail(BaseException(HttpCode.CODE_UNKNOWN, e.message))
            }
        } else {
            //这里最终会调用到 Activity 的showToast
            if (e is JsonSyntaxException) {
                baseViewModel.showToast("服务器返回数据格式异常")

            } else
                baseViewModel.showToast("服务器返回异常:" + e.message)
        }
    }

    override fun onComplete() {
        if (!isExecute) requestCallback?.onSuccess("" as T)
    }
}