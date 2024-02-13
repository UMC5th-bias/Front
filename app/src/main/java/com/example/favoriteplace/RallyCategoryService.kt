package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET

interface RallyCategoryService {
    @GET("/pilgrimage/anime")
    fun getCategory(): Call<List<RallyCategoryResponse>>
}