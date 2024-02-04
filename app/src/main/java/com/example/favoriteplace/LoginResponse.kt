package com.example.favoriteplace

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("grantType") val grantType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)
