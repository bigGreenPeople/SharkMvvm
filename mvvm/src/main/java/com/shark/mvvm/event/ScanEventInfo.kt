package com.shark.event

import java.lang.reflect.Method
import kotlin.reflect.KFunction

/**
 * 扫描方法信息
 */
data class ScanEventInfo(val scanEvent: ScanEvent, val scanFun: Method, val objectThis: Any) {
}