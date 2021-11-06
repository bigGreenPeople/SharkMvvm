package com.shark.retrofit

interface RetrofitWrapper<T> {
    fun initRetrofit(tClass: Class<T>)
    fun getService(): T
}