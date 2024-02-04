package com.example.favoriteplace

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService  {
        @POST("/auth/login")
        suspend fun login(
                @Query("email") email: String,
                @Query("password") password: String
        ): Response<LoginResponse>
}