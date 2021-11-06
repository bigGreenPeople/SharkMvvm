package com.shark.retrofit.interceptor

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset

/**
 * 打印请求日志
 */
class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获得Request
        val request = chain.request()
        val startTime = System.nanoTime()
        Log.d(
            TAG, String.format(
                "Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()
            )
        )
        Log.d(TAG, "intercept url: " + request.url())
        val jsonRequest = Gson().toJson(request.body())
        Log.d(TAG, "intercept request: $jsonRequest")
        //继续发送请求的操作，也就是发送请求 最后能得到Response
        val response = chain.proceed(request)
        val endTime = System.nanoTime()
        val responseBody = response.body()
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE) // request the entire body.
        val buffer = source.buffer()
        // clone buffer before reading from it
        val responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"))
        Log.d(
            TAG,
            "intercept response: $responseBodyString"
        )
        Log.d(
            TAG, String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(), (endTime - startTime) / 1e6, response.headers()
            )
        )
        return response
    }

    companion object {
        const val TAG = "SharkChilli"
    }
}
