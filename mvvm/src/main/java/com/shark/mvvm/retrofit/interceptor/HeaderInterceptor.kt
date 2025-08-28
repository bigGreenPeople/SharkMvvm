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
    private val hostHeaderMap: ConcurrentHashMap<String, ConcurrentHashMap<String, String>> =
        ConcurrentHashMap()

    private val headerMap: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    var TOKEN_NAME = "Authorization"

    fun addHostHeader(host: String, name: String, value: String) {
        if (!hostHeaderMap.contains(host)) {
            hostHeaderMap[host] = ConcurrentHashMap()
        }
        var map = hostHeaderMap[host]
        map?.set(name, value)
    }

    fun removeHostHeader(host: String, name: String, value: String) {
        if (!hostHeaderMap.contains(host)) {
            return
        }
        var map = hostHeaderMap[host]
        map?.remove(name, value)
    }

    fun addHeader(name: String, value: String) {
        headerMap[name] = value
    }

    fun removeHeader(name: String) {
        headerMap.remove(name)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获得Request
        val request = chain.request()

        val requestBuilder = chain.request().newBuilder()


        var url = request.url()

        var hasHost = false;
        // 检测
        hostHeaderMap.forEach { header ->
            var host = header.key
            if (url.toString().startsWith(host)) {
                hasHost = true
                var reHostMap = header.value
                reHostMap.forEach { header ->
                    Log.d(TAG, "host header ${header.key} : ${header.value}")
                    requestBuilder.addHeader(header.key, header.value)
                }
            }
        }

        // 如果已经匹配上面就不要用全局的了
        if (!hasHost) {
            headerMap.forEach { header ->
                Log.d(TAG, "header ${header.key} : ${header.value}")
                requestBuilder.addHeader(header.key, header.value)
            }
        }

        return chain.proceed(requestBuilder.build())
    }

}
