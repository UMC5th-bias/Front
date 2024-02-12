package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET

data class Region(
    val state: String,
    val detail: List<DistrictDetail>
)

data class DistrictDetail(
    val id: Int,
    val district: String
)

interface ApiService {
    @GET("pilgrimage/region")
    fun getRegion(): Call<List<Region>>
}