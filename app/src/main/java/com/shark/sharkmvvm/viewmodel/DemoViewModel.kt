package com.shark.sharkmvvm.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shark.mvvm.service.Service
import com.shark.mvvm.spread.TAG
import com.shark.mvvm.viewmodel.BaseViewModel
import com.shark.sharkmvvm.datasource.UserDataSource
import com.shark.sharkmvvm.service.UserService
import com.shark.tools.room.entity.ScanCode
import com.shark.tools.room.repository.ScanCodeRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DemoViewModel(application: Application) : BaseViewModel(application) {

    val dataString: MutableLiveData<String> = MutableLiveData()

    init {
        //初始化数据
        dataString.value = "SharkChilli"
    }
}