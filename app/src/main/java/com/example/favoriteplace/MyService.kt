package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

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

    @GET("/my/items")
    fun getMyitems(
        @Header("Authorization") authorization: String?,
        @Query("type") type: String
    ): Call<MyItems>

    @GET("/my/guestbooks/like")
    fun getMyLikeRallys(
        @Header("Authorization") authorization: String?,
    ): Call<List<MyRally>>

    @GET("/my/guestbooks/visited")
    fun getMyVisitedRallys(
        @Header("Authorization") authorization: String?,
    ): Call<List<MyRally>>

    @GET("/my/guestbooks/done")
    fun getMyDoneRallys(
        @Header("Authorization") authorization: String?,
    ): Call<List<MyRally>>
}