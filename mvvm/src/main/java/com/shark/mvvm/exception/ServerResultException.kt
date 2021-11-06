package com.mahen.rxjavademo.mvvm.exception

import com.shark.exception.BaseException

class ServerResultException(errorCode: String, errorMessage: String?) :
    BaseException(errorCode, errorMessage) {
}