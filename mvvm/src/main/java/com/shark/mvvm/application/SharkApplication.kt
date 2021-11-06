package com.shark.application

import android.app.Application
import android.content.Context
import com.shark.utils.AppSetting
import com.shark.utils.DeviceInfo

class SharkApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        //设置AppSetting
        AppSetting.application = this
        DeviceInfo.application = this
    }
}