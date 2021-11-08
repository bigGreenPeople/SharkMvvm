package com.shark.mvvm.application

import android.app.Application
import com.shark.mvvm.utils.AppSetting
import com.shark.mvvm.utils.DeviceInfo

open class SharkApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        //设置AppSetting
        AppSetting.application = this
        DeviceInfo.application = this
    }
}