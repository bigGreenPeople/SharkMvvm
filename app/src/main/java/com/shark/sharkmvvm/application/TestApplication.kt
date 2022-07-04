package com.shark.sharkmvvm.application

import com.shark.mvvm.application.SharkApplication
import com.shark.mvvm.config.HttpCode
import com.shark.mvvm.config.HttpConfig
import com.shark.mvvm.crash.SharkCrashHandler
import com.xuexiang.xui.XUI

class TestApplication : SharkApplication() {
    override fun onCreate() {
        super.onCreate()
        HttpCode.CODE_SUCCESS = "200"
        HttpConfig.BASE_URL_WEATHER = "http://192.168.1.123:8081/android/"

//        SharkCrashHandler.init(this, "http://47.111.66.80:8091/android/")
        //设置默认字体为华文行楷
//        XUI.initFontStyle("fonts/hwxk.ttf")

    }
}