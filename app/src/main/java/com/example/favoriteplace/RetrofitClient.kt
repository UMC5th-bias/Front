package com.example.favoriteplace

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL = "http://favoriteplace.store:8080/" // 실제 API 기본 URL로 대체

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val shopService: ShopService = retrofit.create(ShopService::class.java)
    val communityService: CommunityAPIService= retrofit.create(CommunityAPIService::class.java)
}