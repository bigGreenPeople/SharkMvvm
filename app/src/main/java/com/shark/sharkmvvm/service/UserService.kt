package com.shark.sharkmvvm.service

import com.shark.mvvm.retrofit.model.RequestModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * 用户相关的接口
 */
interface UserService {

    @FormUrlEncoded
    @POST("sysUser/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Observable<RequestModel<String>>


    @GET("sysUser/test")
    fun test(
    ): Observable<RequestModel<String>>


}