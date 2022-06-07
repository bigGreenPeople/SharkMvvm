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

//用户信息 SharedPreferences名称
const val USER_PREFS_NAME = "user"
const val LAST_USERNAME = "last_username"


class LoginViewModel(application: Application) : BaseViewModel(application) {
    //共享文件
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
    private val scanCodeRepository: ScanCodeRepository = ScanCodeRepository(application)


    @Service
    private lateinit var userService: UserService

    init {
        GlobalScope.launch {
            scanCodeRepository.synchronizeScanCode()
        }
    }

    lateinit var testList: List<String>
    fun test() {
        call(userService.login("001", "12d3456")) {
            testList.size
        }
    }

}