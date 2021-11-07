package com.shark.mvvm.activity


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
/**
 * 键盘弹出移动布局
 */
annotation class KeyBoardMove(
    val moveId: Int,          //移动的布局id
    val moveToId: Int    //移动到的布局id
)
