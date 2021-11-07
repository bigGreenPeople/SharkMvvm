package com.shark.mvvm.exception

import com.shark.mvvm.exception.BaseException

class ServerResultException(errorCode: String, errorMessage: String?) :
    BaseException(errorCode, errorMessage) {
}