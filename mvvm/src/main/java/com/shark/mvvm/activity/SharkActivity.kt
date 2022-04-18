package com.shark.mvvm.activity


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class SharkActivity(
    val layoutId: Int = 0,          //布局id
    val back: Boolean = true,    //显示返回界面
    val title: String = ""    //标题
)
