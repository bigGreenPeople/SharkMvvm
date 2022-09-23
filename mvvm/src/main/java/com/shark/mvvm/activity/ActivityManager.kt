package com.shark.mvvm.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Process
import com.shark.mvvm.utils.AppSetting
import kotlin.system.exitProcess

object ActivityManager {
    var activities = mutableListOf<Activity>()

    /**
     * 添加Activity
     * @param activity 添加的Activity对象
     */
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    /**
     * 删除Activity
     * @param activity 删除的Activity对象
     */
    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    /**
     * 关闭指定的Activity
     */
    fun finishActivity(activityClazz: Class<*>) {
        //在activities集合中找到类名与指定类名相同的Activity就关闭
        for (activity in activities) {
            val name = activity.javaClass.name//activity的类名
            if (name == activityClazz.name) {
                if (activity.isFinishing) {
                    activities.remove(activity)
                } else {
                    activity.finish()
                }
            }
        }
    }

    /**
     * 关闭所有的Activity
     */
    fun finishAllActivity() {
        for (activity in activities) {
            if (activity.isFinishing) {
                activities.remove(activity)
            } else {
                activity.finish()
            }
        }
    }

    /**
     * 获得启动界面
     */
    fun getLauncherActivity(): Activity? {
        for (activity in activities) {
            if (activity.isTaskRoot) {
                return activity
            }
        }
        return null
    }


}