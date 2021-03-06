package com.shark.sharkmvvm.datasource

import com.shark.mvvm.retrofit.BaseRemoteDataSource
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.sharkmvvm.model.Goods
import com.shark.sharkmvvm.service.ProductService


class DataSource<T>(baseViewModel: BaseViewModel) :
    BaseRemoteDataSource(baseViewModel) {
}