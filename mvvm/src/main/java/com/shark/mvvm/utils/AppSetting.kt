package com.shark.mvvm.utils

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Process
import kotlin.system.exitProcess

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

    /**
     * 重新启动应用程序
     */
    fun restartApp() {
        if (application == null) return
        val intent =
            application?.packageManager?.getLaunchIntentForPackage(application!!.packageName)
        intent!!.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        application?.startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}