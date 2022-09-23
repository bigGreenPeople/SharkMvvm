package com.shark.mvvm.exception

import com.shark.mvvm.config.HttpCode
import com.shark.mvvm.exception.BaseException

class TokenInvalidException(
    errorCode: String = HttpCode.CODE_TOKEN_INVALID,
    errorMessage: String = "Token失效"
) : BaseException(errorCode, errorMessage) {
}