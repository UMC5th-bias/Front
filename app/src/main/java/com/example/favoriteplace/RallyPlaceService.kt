package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RallyPlaceService {
    @GET("/pilgrimage/region")
    fun getRegion(): Call<List<Region>>

    @GET("/pilgrimage/region/{regionId}")
    fun getAnimationList(
        @Path("regionId") regionId: Long
    ): Call<List<RallyPlaceAnimation>>

}