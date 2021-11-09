package com.shark.sharkmvvm.service

import com.shark.sharkmvvm.model.Goods
import com.shark.sharkmvvm.model.RequestModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface ProductService {
    @GET("cashRegister/commodityPrices")
    fun shopOrders(): Observable<RequestModel<List<Goods>>>


}