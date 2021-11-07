package com.shark.mvvm.retrofit

import com.shark.mvvm.retrofit.interceptor.LoggingInterceptor
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * 生成Retrofit接口统一处理的类
 *
 * @param T 生成的接口类型
 * @property TAG String
 * @property DEFAULT_TIME Int 请求超时时间
 * @property url String 总的url
 * @property retrofit Retrofit? Retrofit对象引用
 * @property tClass Class<T>?
 * @property service T? 生成的接口对象引用
 */

@Deprecated("Please Use RetrofitManagement")
class BaseRetrofit<T>(val tClass: Class<T>) : RetrofitWrapper<T> {
    private val TAG = "SharkChilli"
    private val DEFAULT_TIME: Long = 1000 * 60
    private val url: String = "http://digitinterface.qlaisoft.com/api/pda/"
    private var retrofit: Retrofit? = null
    private var service: T? = null

    init {
        initRetrofit(tClass)
    }

    /**
     * 初始化 Retrofit
     * @param tClass Class<T>
     */
    override fun initRetrofit(tClass: Class<T>) {
        //调度器
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 10
        dispatcher.maxRequestsPerHost = 10


        //添加拦截器
        val loggingInterceptor = LoggingInterceptor()

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS) //设置读取超时时间
            .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS) //设置请求超时时间
            .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS) //设置写入超时时间
            .addInterceptor(loggingInterceptor) //添加打印拦截器
            .dispatcher(dispatcher)
            .build()

        //创建retrofit实例
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .baseUrl(url)
            .build()

        //创建请求接口实例
        service = retrofit!!.create(tClass)
    }

    /**
     * 得到指定的Service
     * @return T
     */
    override fun getService(): T {
        return service ?: retrofit?.create(tClass)!!.also { service = it }
    }
}