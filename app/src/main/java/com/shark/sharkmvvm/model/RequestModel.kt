package com.shark.sharkmvvm.model

import com.google.gson.annotations.SerializedName
import com.shark.mvvm.retrofit.model.BaseRequestModel

data class RequestModel<T>(
    @SerializedName("status")
    override val type: String = "0",
    @SerializedName("message")
    override val msg: String,
    override val data: T,

    ) : BaseRequestModel<T> {
    private val count = 0
    private val totalCount = 0
    private val empty = false
    private val success = false
}