package com.shark.eventbus

@Deprecated("Please Use Rxjava")
object EventBusEnabledHandler {
    /**
     * 判断对象是否有EventBusEnabled注解标记
     * @param eventObject Any
     * @return Boolean
     */
    fun isEventBusEnabled(eventObject: Any): Boolean {
        return eventObject.javaClass.isAnnotationPresent(EventBusEnabled::class.java)
    }
}