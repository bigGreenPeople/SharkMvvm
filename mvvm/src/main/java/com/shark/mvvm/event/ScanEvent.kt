package com.shark.mvvm.event

import android.view.KeyEvent


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ScanEvent(
    val id: Int = 0,    //接收扫描时间的id
    val code: Int = 0,    //按键的code
    val action: Int = KeyEvent.ACTION_UP,    //执行动作 默认是抬起
    val scanNull: Boolean = false,    //扫描结果是否能为空
    val clean: CleanModel = CleanModel.ERROR //请求后是否自动清除 这个值在一次请求时使用(多次请求请在回调函数中处理)
)
