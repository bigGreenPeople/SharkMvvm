package com.shark.retrofit.subscriber

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.shark.viewmodel.BaseViewModel
import io.reactivex.observers.DisposableObserver
import com.shark.config.HttpCode
import com.shark.exception.BaseException


import com.shark.retrofit.callback.RequestCallback
import com.shark.retrofit.callback.RequestMultiplyCallback


/**
 * 订阅者 或者叫 监听者
 * @param T
 */
class BaseRemoteSubscriber<T>(
    private val baseViewModel: BaseViewModel,
    var requestCallback: RequestCallback<T>? = null
) : DisposableObserver<T>() {

    //成功执行设置的回调
    override fun onNext(t: T) {
        requestCallback?.onSuccess(t)
    }

    /**
     * 数据过滤抛出异常时会来到这
     * 这里如果设置的回调为 RequestMultiplyCallback类型就执行 onFail的逻辑
     * @param e Throwable
     */
    override fun onError(e: Throwable) {
        Log.i("BaseSubscriber", "onError: ", e)

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
    }
}