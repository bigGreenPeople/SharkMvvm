package com.shark.mvvm.retrofit.interceptor

import android.util.Log
import com.shark.mvvm.spread.TAG
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

/**
 * 请求头拦截器
 */
object HeaderInterceptor : Interceptor {
    private val headerMap: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    fun addHeader(name: String, value: String) {
        headerMap[name] = value
    }

    fun removeHeader(name: String) {
        headerMap.remove(name)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获得Request
        val requestBuilder = chain.request().newBuilder()
        headerMap.forEach { header ->
            Log.d(TAG, "header ${header.key} : ${header.value}")
            requestBuilder.addHeader(header.key, header.value)
        }
        return chain.proceed(requestBuilder.build())
    }

}
