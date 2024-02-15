package com.example.favoriteplace

import com.example.favoriteplace.RetrofitAPI.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // BODY 레벨로 설정하여 요청/응답의 본문 내용까지 로그로 출력
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private const val BASE_URL = "http://favoriteplace.store:8080/" // 실제 API 기본 URL로 대체

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val shopService: ShopService = retrofit.create(ShopService::class.java)

    val postService: PostService = retrofit.create(PostService::class.java)

    val communityService: CommunityAPIService= retrofit.create(CommunityAPIService::class.java)



}