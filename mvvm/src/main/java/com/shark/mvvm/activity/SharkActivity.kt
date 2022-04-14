package com.shark.mvvm.activity


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class SharkActivity(
    val value: String,       //路由跳转规则 此参数仅在组件化时起到作用
    val layoutId: Int = 0,          //布局id
    val back: Boolean = true,    //显示返回界面
    val title: String = ""    //标题
)
