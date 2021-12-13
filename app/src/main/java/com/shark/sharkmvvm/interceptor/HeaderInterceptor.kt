package com.shark.sharkmvvm.interceptor

import android.util.Log
import com.google.gson.Gson
import com.shark.mvvm.spread.TAG
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset

/**
 * 打印请求日志
 */
class HeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获得Request
        val request = chain.request()
        Log.d(TAG, "intercept request: HeaderInterceptor")
        val response = chain.proceed(request)
        return response
    }

}
