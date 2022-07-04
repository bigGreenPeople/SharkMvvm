package com.shark.sharkmvvm.service

import com.shark.mvvm.retrofit.model.RequestModel
import com.shark.sharkmvvm.model.User
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * 测试相关的接口
 */
interface TestService {

    @GET("test/index")
    fun test(
        @Query("type") type: String
    ): Observable<RequestModel<User>>

}