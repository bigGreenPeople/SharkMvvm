package com.shark.mvvm.retrofit.model

data class RequestModel<T>(
    override val type: String,
    override val msg: String,
    override val data: T
) : BaseRequestModel<T> {
}