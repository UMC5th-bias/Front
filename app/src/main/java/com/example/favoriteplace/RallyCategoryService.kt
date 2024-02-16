package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RallyCategoryService {
    @GET("/pilgrimage/anime")
    fun getCategory(
        @Header("Authorization") authorization: String?
    ): Call<List<RallyCategoryResponse>>
}