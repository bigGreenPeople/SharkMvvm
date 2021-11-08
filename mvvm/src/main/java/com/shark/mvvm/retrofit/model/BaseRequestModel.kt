package com.shark.mvvm.retrofit.model

interface BaseRequestModel<T> {
    val type: String
    val msg: String
    val data: T
}