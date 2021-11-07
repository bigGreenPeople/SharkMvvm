package com.shark.mvvm.viewmodel

/**
 * 此类用于抽象消息
 * @property action Int
 * @constructor
 */
open class BaseEvent(val action: Int)

/**
 * BaseActionEvent 即用于向 View 层传递 Action 的 Model
 * 在 ViewModel 通过向 View 层传递不同的消息类型，从而触发相对应的操作。
 * @property SHOW_LOADING_DIALOG Int
 * @property DISMISS_LOADING_DIALOG Int
 * @property SHOW_TOAST Int
 * @property FINISH Int
 * @property FINISH_WITH_RESULT_OK Int
 * @property message String?
 * @constructor
 */
class BaseActionEvent(action: Int) : BaseEvent(action) {
    companion object{
        val SHOW_LOADING_DIALOG = 1
        val DISMISS_LOADING_DIALOG = 2
        val SHOW_TOAST = 3
        val FINISH = 4
        val FINISH_WITH_RESULT_OK = 5
    }

    var message: String? = null

}