package com.shark.tools.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shark.mvvm.viewmodel.BaseViewModel


class ScanKeyCodeViewModel(application: Application) : BaseViewModel(application) {
    //0是等待扫描 1 是扫描多 2扫描成功 3添加成功
    val status: MutableLiveData<Int> = MutableLiveData()

    init {
        status.value = 0
    }

}