package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET

interface CommunityHomeService {
    @GET("/posts/trending/today/free")
    fun getTrendingFree(): Call<CommunityHomeTrendingFree>

    @GET("/posts/trending/today/guestbooks")
    fun getTrendingGuestbook(): Call<CommunityHomeTrendingGuestbook>

    @GET("/posts/trending/month")
    fun getTrendingMonth(): Call<List<CommunityHomeTrendingMonthUnit>>
}