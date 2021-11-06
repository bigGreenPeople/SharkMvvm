package com.shark.viewmodel

/**
 * 此方法标志的属性不能使用 lateinit
 * 如果你的父类Activity继承了BaseActivity他就会自动注入ViewModel
 * 如果你的父类Activity继承了MvvmActivity他就会自动监听LifecycleOwner
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class SharkViewModel(
)
