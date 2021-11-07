package com.shark.mvvm.retrofit.model

data class RequestModel<T>(
    val type: String,
    val msg: String,
    val data: T
)