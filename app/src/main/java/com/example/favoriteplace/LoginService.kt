package com.example.favoriteplace


import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {


    data class LoginResponse(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String,

        val errorMessage: String? // 실패 시
    )


    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>
}