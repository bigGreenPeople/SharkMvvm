package com.shark.retrofit.callback

import com.shark.exception.BaseException

interface RequestCallback<T> {
    fun onSuccess(t: T)
}

interface RequestMultiplyCallback<T> : RequestCallback<T> {
    fun onFail(e: BaseException?)
}