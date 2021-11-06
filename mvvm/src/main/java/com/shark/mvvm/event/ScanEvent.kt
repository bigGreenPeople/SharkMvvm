package com.shark.event


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ScanEvent(
    val id: Int = 0,    //接收扫描时间的id
    val clean: Boolean = true //请求后是否自动清除 这个值在一次请求时使用(多次请求请在回调函数中处理)
)
