package com.shark.sharkmvvm.proxy

import android.util.Log
import com.shark.mvvm.exception.BaseException
import com.shark.mvvm.retrofit.RetrofitManagement
import com.shark.mvvm.retrofit.model.BaseRequestModel
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.sharkmvvm.datasource.ProxyDataSource
import io.reactivex.Observable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

class ProxyService<T>(val clz: Class<T>, val baseViewModel: BaseViewModel) {
    private val target: T? = RetrofitManagement.getService(clz)
    private val proxyDataSource = ProxyDataSource<T>(baseViewModel)

    fun getProxyDataSourceInstance(): T {
        return Proxy.newProxyInstance(
            target!!.javaClass.classLoader,
            target.javaClass.interfaces, InvocationHandler { _, method, args ->
                val result = method.invoke(
                    target,
                    *args.orEmpty()
                ) as Observable<BaseRequestModel<Any>>

                proxyDataSource.execute(
                    result,
                    successCallback = successCallback
                )
                result
            }) as T
    }

    companion object {
        var successCallback: ((result: Any) -> Unit)? = null
    }
}
