package com.shark.activity


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class SharkActivity(
    val layoutId: Int,          //布局id
    val back: Boolean = true    //显示返回界面
)
