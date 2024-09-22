package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApiService {
    @POST("registerToken")
    fun registerToken(@Body token: TokenRequest): Call<Void>

    @POST("unregisterToken")
    fun unregisterToken(@Body token: TokenRequest): Call<Void>
}