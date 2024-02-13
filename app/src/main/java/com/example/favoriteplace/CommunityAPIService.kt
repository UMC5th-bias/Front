package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityAPIService {
    @GET("posts/free?")
    fun getPosts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Call<CommunityPost>

}