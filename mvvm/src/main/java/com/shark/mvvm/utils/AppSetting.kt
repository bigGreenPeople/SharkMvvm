package com.shark.mvvm.utils

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object AppSetting {
    var application: Application? = null

    val DEBUG: Boolean
        get() =
            (application?.applicationInfo?.flags?.and(ApplicationInfo.FLAG_DEBUGGABLE)) != 0

    //当前版本号
    val versionName: String?
        get() = application?.let {
            it.packageManager.getPackageInfo(
                it.packageName,
                PackageManager.GET_CONFIGURATIONS
            ).versionName
        }

}