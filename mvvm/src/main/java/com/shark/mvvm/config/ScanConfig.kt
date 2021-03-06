package com.shark.mvvm.config

import android.view.KeyEvent

/**
 * 扫描配置
 */
object ScanConfig {
    //屏蔽事件
    val closeSystemKeySet: MutableSet<Int> = mutableSetOf(
        KeyEvent.KEYCODE_ENTER
    )

    //存储所有定义的扫描按键
    val keySet: MutableSet<Int> = mutableSetOf(
        288, 289, 285, 286,
        305,
        KeyEvent.KEYCODE_UNKNOWN, KeyEvent.KEYCODE_ENTER
    )
}