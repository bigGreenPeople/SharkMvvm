package com.shark.exception

import com.shark.config.HttpCode
import java.lang.RuntimeException

open class BaseException : RuntimeException {
    //默认为位置错误
    var errorCode: String = HttpCode.CODE_UNKNOWN
        private set

    constructor() {}
    constructor(errorCode: String, errorMessage: String?) : super(errorMessage) {
        this.errorCode = errorCode
    }
}