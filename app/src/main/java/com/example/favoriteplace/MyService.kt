package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET

interface MyService {
    @POST("/auth/logout")
    fun logout(
        @Header("Authorization") authorization: String?
    ): Call<String>

    @GET("/my")
    fun getMyInfo(
        @Header("Authorization") authorization: String?
    ): Call<MyInfo>

    @GET("/my/profile")
    fun getMyProfile(
        @Header("Authorization") authorization: String?
    ): Call<MyProfile>
}