package com.shark.exception

import com.shark.config.HttpCode

class TokenInvalidException: BaseException(HttpCode.CODE_TOKEN_INVALID, "Token失效") {
}