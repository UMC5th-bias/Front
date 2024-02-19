package com.example.favoriteplace

import com.example.favoriteplace.RetrofitAPI.retrofit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitAPI {

    private const val BASE_URL = "http://favoriteplace.store:8080"

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }



    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    val signUpService: SignUpService = retrofit.create(SignUpService::class.java)

    val loginService: LoginService = retrofit.create(LoginService::class.java)

    val rallyPlaceService: RallyPlaceService = retrofit.create(RallyPlaceService::class.java)
    val rallyHomeService: RallyHomeService = retrofit.create(RallyHomeService::class.java)
    val rallyCategoryService: RallyCategoryService = retrofit.create(RallyCategoryService::class.java)
    val rallyDetailService: RallyDetailService = retrofit.create(RallyDetailService::class.java)

    val communityHomeService: CommunityHomeService = retrofit.create(CommunityHomeService::class.java)

    val myService: MyService = retrofit.create(MyService::class.java)

    val rallyLocationDetailService: RallyLocationDetailService = retrofit.create(RallyLocationDetailService::class.java)

}




