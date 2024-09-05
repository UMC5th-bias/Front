package com.example.favoriteplace


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {


    data class LoginResponse(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String,
        val errorMessage: String? // 실패 시
    )


    data class KakaoLoginResponse(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String
    )


    @POST("/auth/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<LoginResponse>


    @POST("/auth/login/kakao")
    fun kakaoLogin(
        @Header("Authorization") authorization: String,
        @Body tokenData: Map<String, String?>
    ): Call<KakaoLoginResponse>
}