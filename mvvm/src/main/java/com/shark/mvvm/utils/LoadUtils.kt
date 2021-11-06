package com.shark.utils

import android.app.Activity
import android.graphics.Color
import com.zyao89.view.zloading.ZLoadingDialog
import com.zyao89.view.zloading.Z_TYPE

object LoadUtils {
    private var pDialog: ZLoadingDialog? = null

    /**
     * 加载框设置
     * @param activity Activity
     * @param tip String
     * @param time Long 设置指定时间后自动关闭 默认不自动关闭
     */
    fun load(activity: Activity, tip: String = "加载中", time: Long = 0L) {
        //加载load框
        pDialog = ZLoadingDialog(activity).apply {
            setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE) //设置类型
            setLoadingColor(Color.WHITE) //颜色
            setHintText(tip)
            setHintTextSize(16f) // 设置字体大小 dp
            setCanceledOnTouchOutside(false)
            setHintTextColor(Color.WHITE) // 设置字体颜色
            setDialogBackgroundColor(Color.parseColor("#00000000")) // 设置背景色，默认白色
            show()

            if (time != 0L) {
                setTimeout(time) { cancel() }
            }
        }


    }

    /**
     * 关闭加载框
     */
    fun cancel() {
        pDialog!!.cancel()
        pDialog = null
    }
}