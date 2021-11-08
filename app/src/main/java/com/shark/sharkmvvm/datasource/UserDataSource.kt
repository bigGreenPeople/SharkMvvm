package com.shark.sharkmvvm.datasource

import com.shark.mvvm.retrofit.BaseRemoteDataSource
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.sharkmvvm.model.Goods
import com.shark.sharkmvvm.service.ProductService


class UserDataSource(baseViewModel: BaseViewModel) : BaseRemoteDataSource(baseViewModel) {
    fun shopOrders(
        successCallback: ((result: List<Goods>) -> Unit)? = null
    ) {
        execute(
            getService(ProductService::class.java).shopOrders(),
            successCallback = successCallback
        )
    }
}