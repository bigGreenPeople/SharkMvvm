package com.shark.mvvm.exception

import com.shark.mvvm.config.HttpCode
import java.lang.RuntimeException

open class BaseException : RuntimeException {
    //默认为位置错误
    var errorCode: String = HttpCode.CODE_UNKNOWN
        private set

    var errorMessage: String? = null

    constructor() {}
    constructor(errorCode: String, errorMessage: String?) : super(errorMessage) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage

    }
}