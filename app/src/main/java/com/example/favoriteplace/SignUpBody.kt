package com.example.favoriteplace

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody



data class SignUpBody(
    @SerializedName("nickname")
    val nickname:  String,

    @SerializedName("email")
    val email : String,

    @SerializedName("password")
    val password : String,

    @SerializedName("snsAllow")
    val snsAllow : Boolean,

    @SerializedName("introduction")
    val introduction : String,

    // images 필드 추가
    @SerializedName("images")
    val images: List<MultipartBody.Part>?

)
