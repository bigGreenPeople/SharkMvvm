package com.shark.mvvm.event

enum class CleanModel {
    ERROR,  //请求业务错误时清除
    ALWAYS, //调用完扫描事件总是清除
    NEVER,  //无论何时都不清除
}