package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CommunityAPIService {
    @GET("posts/free?")
    fun getPosts(
        @Header("Authorization") authorization:String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Call<CommunityPost>

    @GET("posts/free/my-posts")
    fun getMyPosts(
        @Header("Authorization") authorization:String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<CommunityPost>

    @GET("posts/free/my-comments")
    fun getMyComments(
        @Header("Authorization") authorization:String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<CommunityComment>

}