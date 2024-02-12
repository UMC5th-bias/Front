package com.example.favoriteplace

data class SignUpResponse(
    val token: String?,

    val status: String,
    val httpStatus : String,
    val code : Int,
    val message : String,
)
