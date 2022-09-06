package com.shark.mvvm.retrofit.header

import android.content.Context
import com.shark.mvvm.retrofit.interceptor.HeaderInterceptor
import com.shark.mvvm.utils.SystemInfoUtils
import com.shark.mvvm.utils.isTablet

object DeviceHeader {
    fun init(context: Context) {
        val systemInfoUtils = SystemInfoUtils(context)

        //设备类型 平板 tablet、手机 phone
        if (isTablet(context)) {
            HeaderInterceptor.addHeader("DEVICE_TYPE", "tablet")
        } else {
            HeaderInterceptor.addHeader("DEVICE_TYPE", "phone")
        }
        //设备型号
        HeaderInterceptor.addHeader("DEVICE_MODEL", android.os.Build.MODEL)
        //设备品牌
        HeaderInterceptor.addHeader("DEVICE_BRAND", android.os.Build.BRAND)
        //APP版本名称
        HeaderInterceptor.addHeader("APP_VERSION_NAME", systemInfoUtils.versionName)
        //APP版本Code
        HeaderInterceptor.addHeader("APP_VERSION_CODE", systemInfoUtils.versionCode.toString())
        //系统
        HeaderInterceptor.addHeader("DEVICE_OS", "android")
        //系统SDK版本
        HeaderInterceptor.addHeader("DEVICE_OS_VERSION", android.os.Build.VERSION.SDK_INT.toString())
        //系统版本名称
        HeaderInterceptor.addHeader("DEVICE_OS_NAME", android.os.Build.VERSION.RELEASE)
    }
}