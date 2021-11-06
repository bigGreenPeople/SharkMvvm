package com.shark.utils

import java.util.*


/**
 * 延时执行
 * @param long Long 延时时间
 * @param taskFun Function1<[@kotlin.ParameterName] Timer, Unit> 执行的方法
 */
fun setTimeout(delay: Long = 5000, taskFun: (timer: Timer) -> Unit): Timer  {
    val timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            taskFun.invoke(timer)
        }
    }, delay)

    return timer
}

/**
 * 延时重复执行
 * @param long Long 延时时间
 * @param period Long 重复间隔时间
 * @param taskFun Function1<[@kotlin.ParameterName] Timer, Unit> 执行的方法
 *
 * @return
 */
fun schedule(delay: Long = 0, period: Long = 1000, taskFun: (timer: Timer) -> Unit): Timer {
    val timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            taskFun.invoke(timer)
        }
    }, delay, period)
    return timer
}