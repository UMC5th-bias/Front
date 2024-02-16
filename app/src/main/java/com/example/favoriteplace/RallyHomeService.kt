package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RallyHomeService {
    @GET("/pilgrimage/trending")
    fun getTrending(
        @Header("Authorization") authorization: String?
    ): Call<RallyHomeTrending>

    @GET("/pilgrimage")
    fun getMyRally(): Call<RallyHomeResponseMyRally>
}