package com.shark.retrofit

/**
 * 作为任务的调度器(我们可以随时更改remoteDataSource的实现)主要用来存一下remoteDataSource的引用
 * @param T
 * @property remoteDataSource T
 * @constructor
 */
open class BaseRemoteRepo<T>(var remoteDataSource: T) {
}