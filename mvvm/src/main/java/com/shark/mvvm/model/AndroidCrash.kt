package com.shark.mvvm.model

import android.content.Context
import android.os.Build
import com.shark.mvvm.utils.SystemInfoUtils

data class AndroidCrash(
    //日志文件路径
    var logPath: String? = null,

    //设备品牌

    var buildBrand: String? = null,

    //设备型号

    var buildModel: String? = null,

    //APP版本名称

    var appVersionName: String? = null,

    //APP版本Code

    var versionCode: String? = null,

    //路由(报错页面)关键信息

    var routeError: String? = null,

    //系统SDK版本

    var sdkVersion: String? = null,

    //系统版本名称
    var systemVersionName: String? = null,
) {
    fun getDeviceCrash(mContext: Context, logPath: String?): AndroidCrash {
        val systemInfoUtils = SystemInfoUtils(mContext)

        this.buildBrand = Build.BRAND
        this.appVersionName = systemInfoUtils.versionName
        this.buildModel = Build.MODEL
        this.logPath = logPath
        this.sdkVersion = Build.VERSION.SDK_INT.toString()
        this.versionCode = systemInfoUtils.versionCode.toString()
        this.systemVersionName = Build.VERSION.RELEASE
        this.routeError = systemInfoUtils.topActivity
        return this
    }
}
