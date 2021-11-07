package com.shark.mvvm.retrofit.callback

import com.shark.mvvm.exception.BaseException

interface RequestCallback<T> {
    fun onSuccess(t: T)
}

interface RequestMultiplyCallback<T> : RequestCallback<T> {
    fun onFail(e: BaseException?)
}