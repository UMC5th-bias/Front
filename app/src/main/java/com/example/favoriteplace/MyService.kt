package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MyService {
    @POST("/auth/logout")
    fun logout(
        @Header("Authorization") authorization: String?
    ): Call<String>
}