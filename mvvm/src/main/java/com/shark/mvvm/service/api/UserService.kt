package com.shark.mvvm.service.api

import com.shark.mvvm.model.AndroidCrash
import com.shark.mvvm.retrofit.model.RequestModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * App相关的接口
 */
interface UserService {
    @GET("sysUser/renewToken")
    fun renewToken(): Observable<RequestModel<String>>

}