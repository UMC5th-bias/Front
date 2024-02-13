package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET

interface RallyHomeService {
    @GET("/pilgrimage/trending")
    fun getTrending(): Call<RallyHomeTrending>

    @GET("/pilgrimage")
    fun getMyRally(): Call<RallyHomeResponseMyRally>
}