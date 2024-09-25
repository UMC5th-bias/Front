package com.example.favoriteplace

import com.google.gson.annotations.SerializedName

data class ShopDetailsResponse(
    val saleDeadline: String,
    val category: String,
    val imageUrl: String,
    val imageCenterUrl: String,
    val userPoint: Int,
    val status: String,
    val name: String,
    val point: Int,
    val description: String,
    val alreadyBought: Boolean
)

data class PurchaseResponse(
    val canBuy: Boolean
)

data class ApplyResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("httpStatus")
    val httpStatus: String? = null,

    @SerializedName("code")
    val code: String? = null
)