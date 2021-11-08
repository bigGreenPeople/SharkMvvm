package com.shark.sharkmvvm.model

data class Goods(
    val id: Int = 0,
    val skuId: Int = 0,
    val skuImage: String? = null,
    val skuImageUrl: String? = null,
    val commodityId: Int = 0,
    val commodityName: String? = null,
    val commodityIdentifier: String? = null,
    val size: String? = null,
    val colourName: String? = null,
    val price: Float = 0f,
    val sort: Int = 0,
    val type: String? = null,
    val remark: String? = null
) {

}