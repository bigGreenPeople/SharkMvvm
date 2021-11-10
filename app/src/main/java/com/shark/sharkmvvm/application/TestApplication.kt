package com.shark.sharkmvvm.application

import com.shark.mvvm.application.SharkApplication
import com.shark.mvvm.config.HttpCode
import com.shark.mvvm.config.HttpConfig
import com.xuexiang.xui.XUI

class TestApplication : SharkApplication() {
    override fun onCreate() {
        super.onCreate()
        HttpConfig.BASE_URL_WEATHER = "http://retailtest.qingyizhu.net/api/"
        HttpCode.CODE_SUCCESS = "200"

        //设置默认字体为华文行楷
//        XUI.initFontStyle("fonts/hwxk.ttf")

    }
}