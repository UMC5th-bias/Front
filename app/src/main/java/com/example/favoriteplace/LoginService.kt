package com.example.favoriteplace


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {


    data class LoginResponse(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String,
        val errorMessage: String? // 실패 시
    )



    @POST("/auth/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<LoginResponse>
}