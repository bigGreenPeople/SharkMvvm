package com.shark.mvvm.exception

import com.shark.mvvm.config.HttpCode
import com.shark.mvvm.exception.BaseException

class TokenInvalidException: BaseException(HttpCode.CODE_TOKEN_INVALID, "Token失效") {
}