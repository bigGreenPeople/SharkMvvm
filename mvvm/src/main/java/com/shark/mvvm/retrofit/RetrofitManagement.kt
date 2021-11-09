package com.shark.mvvm.retrofit

import com.shark.mvvm.exception.ServerResultException
import com.shark.mvvm.exception.TokenInvalidException
import com.shark.mvvm.retrofit.interceptor.LoggingInterceptor
import io.reactivex.*
import java.util.concurrent.ConcurrentHashMap
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.schedulers.Schedulers

import io.reactivex.functions.Function
import java.lang.Exception
import com.shark.mvvm.config.HttpCode
import com.shark.mvvm.config.HttpConfig
import com.shark.mvvm.retrofit.model.BaseRequestModel


object RetrofitManagement {
    //读超时时间
    private const val READ_TIMEOUT: Long = 6000

    //写超时间
    private const val WRITE_TIMEOUT: Long = 6000

    //连接超时时间
    private const val CONNECT_TIMEOUT: Long = 6000

    //定义线程安全的HashMap
    private val serviceMap: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    /**
     * 此方法构建出Retrofit对象 并对其做了基本的配置
     * @param url String
     * @return Retrofit?
     */
    private fun createRetrofit(url: String): Retrofit? {
        val builder = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(LoggingInterceptor())
            .retryOnConnectionFailure(true)
        //TODO debug 添加不同的Interceptor
//        if (BuildConfig.DEBUG) {
//            builder.addInterceptor(LoggingInterceptor())
//        }
        val client = builder.build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    /**
     * 做一个ObservableTransformer 被传递的数据在io线程中执行 最后给监听者在Ui线程执行
     * 在交给监听者前flatMap会对数据进行过滤 (此处主要是用于返回值的统一判断)错误的返回值抛出异常
     * @return ObservableTransformer<RequestModel<T>, T>?
     */
    fun <T, M : BaseRequestModel<T>> applySchedulers(): ObservableTransformer<M, T>? {
        return ObservableTransformer<M, T> { observable: Observable<M> ->
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Function<M, ObservableSource<out T>> { result: M ->
                    when (result.type) {
                        HttpCode.CODE_SUCCESS -> {
                            //如果成功则创建数据被观察者
                            return@Function createData(result.data)
                        }
                        HttpCode.CODE_TOKEN_INVALID -> {
                            throw TokenInvalidException()
                        }
                        else -> {
                            throw ServerResultException(
                                result.type,
                                result.msg
                            )
                        }
                    }
                })
        }
    }

    /**
     * 创建数据的被观察者
     * @param t T
     * @return Observable<T>?
     */
    private fun <T> createData(t: T): Observable<T>? {
        return Observable.create { emitter ->
            try {
                emitter.onNext(t)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    /**
     * 获取服务的接口 直接使用配置总的url
     * @param clz Class<T>
     * @return T
     */
    fun <T> getService(clz: Class<T>): T {
        return getService(clz, HttpConfig.BASE_URL_WEATHER!!)
    }

    /**
     * 获得服务接口 已经获取的接口会被缓存起来
     * @param clz Class<T>?
     * @param host String?
     * @return T
     */
    fun <T> getService(clz: Class<T>, host: String): T {
        val value: T
        //生成key
        val key = "${host}_${clz.name}"

        if (serviceMap.containsKey(key)) {
            val obj = serviceMap[key]
            if (obj == null) {
                value = createRetrofit(host)!!.create(clz)
                serviceMap[key] = value!!
            } else {
                value = obj as T
            }
        } else {
            value = createRetrofit(host)!!.create(clz)
            serviceMap[host] = value!!
        }
        return value
    }
}