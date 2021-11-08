package com.shark.sharkmvvm.service

import com.shark.sharkmvvm.model.Goods
import com.shark.sharkmvvm.model.RequestModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface ProductService {
    //点检保养确认
    @GET("cashRegister/commodityPrices")
    fun shopOrders(): Observable<RequestModel<List<Goods>>>


}