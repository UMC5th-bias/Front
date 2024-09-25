package com.example.favoriteplace

import com.google.gson.annotations.SerializedName

data class PostDetail(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("httpStatus")
    val httpStatus: String? = null,

    @SerializedName("code")
    val code: String? = null
)
