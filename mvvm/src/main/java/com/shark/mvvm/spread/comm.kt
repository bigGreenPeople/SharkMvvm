package com.shark.spread

import android.text.TextUtils
import android.widget.Toast
import com.shark.utils.AppSetting

const val TAG = "SharkChilli"

//设置为String的扩展函数只要是String类型就可以直接使用 "".showToast()的格式
//但是这样封装不能过把之前的message顶掉影响使用
fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    //MyApplication 这是一个直接获取全局context的类
    Toast.makeText(AppSetting.application, this, Toast.LENGTH_SHORT).show()
}

/**
 * 判断字符串不是空
 * @param text String?
 * @return Boolean
 */
fun isNotEmpty(text: String?): Boolean {
    return (!TextUtils.isEmpty(text)) && text!!.isNotBlank()
}

/**
 * 判断字符串是空
 * @param text String?
 * @return Boolean
 */
fun isEmpty(text: String?): Boolean {
    return !isNotEmpty(text)
}