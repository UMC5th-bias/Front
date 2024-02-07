package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET

interface RallyPlaceService {
    @GET("pilgrimage/region")
    fun getRegion(): Call<List<Region>>
}