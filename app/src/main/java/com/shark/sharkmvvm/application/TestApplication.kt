package com.shark.sharkmvvm.application

import com.shark.mvvm.application.SharkApplication
import com.shark.mvvm.config.HttpConfig

class TestApplication : SharkApplication() {
    override fun onCreate() {
        super.onCreate()
        HttpConfig.BASE_URL_WEATHER = "http://retailtest.qingyizhu.net/api/"
    }
}