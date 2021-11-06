package com.shark.utils

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import java.security.NoSuchAlgorithmException

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