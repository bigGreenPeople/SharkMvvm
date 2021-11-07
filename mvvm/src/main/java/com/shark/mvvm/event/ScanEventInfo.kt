package com.shark.mvvm.event

import com.shark.mvvm.event.ScanEvent
import java.lang.reflect.Method

/**
 * 扫描方法信息
 */
data class ScanEventInfo(val scanEvent: ScanEvent, val scanFun: Method, val objectThis: Any) {
}