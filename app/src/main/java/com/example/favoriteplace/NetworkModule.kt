package com.example.favoriteplace

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL="http://43.203.89.241:8080"

//retrofit 객체 생성, retrofit 객체 반환
fun getRetrofit():Retrofit{
    val retrofit=Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}