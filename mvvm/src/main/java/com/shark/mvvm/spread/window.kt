package com.shark.spread

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView

/**
 * 弹窗
 * @param context Context
 * @param layOutId Int
 * @param viewSetting Function1<[@kotlin.ParameterName] View, Unit>? 在此回调中设置你界面的相关信息
 */
fun popWindow(
    context: Context,
    layOutId: Int,
    viewSetting: ((view: View, dialog: AlertDialog) -> Unit)? = null
) {
    val view: View =
        LayoutInflater.from(context).inflate(layOutId, null, false)
    val dialog = AlertDialog.Builder(context).setView(view).create()

//    dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
    viewSetting?.invoke(view, dialog)
    dialog.show()
}
