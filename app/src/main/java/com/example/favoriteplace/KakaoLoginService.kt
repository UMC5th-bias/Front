package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface KakaoLoginService {

    data class KakaoLoginResponse(
        val accessToken: String,
        val refreshToken: String,
        val errorMessage: String? // 실패 시
    )



    @POST("/auth/login/kakao")
    fun kakakoLogin(
        @Query("email") email: String,
        @Query("password") password: String,
    ): Call<KakaoLoginResponse>
}