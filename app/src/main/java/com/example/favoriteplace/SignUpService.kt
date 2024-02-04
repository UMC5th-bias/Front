package com.example.favoriteplace

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SignUpService {
    @Multipart
    @POST("/auth/signup")
    suspend fun addSignup(
        @Part("data") userData: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<SignUpResponse>

}