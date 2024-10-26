package com.example.favoriteplace

import com.example.favoriteplace.RetrofitAPI.okHttpClient
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // BODY 레벨로 설정하여 요청/응답의 본문 내용까지 로그로 출력
    }

    // TODO 실제 주소로 변경해야함.
    private const val BASE_URL = "http://favoriteplace.store:8080" // 실제 API 기본 URL로 대체

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())  // 추가
        .client(okHttpClient)
        .build()

    val shopService: ShopService by lazy {
        retrofit.create(ShopService::class.java)
    }

    val postService: PostService by lazy {
        retrofit.create(PostService::class.java)
    }

    val communityService: CommunityAPIService by lazy {
        retrofit.create(CommunityAPIService::class.java)
    }

    val notificationApiService: NotificationApiService by lazy {
        retrofit.create(NotificationApiService::class.java)
    }

}