package com.example.favoriteplace

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {
    @Multipart
    @POST("/posts/free")
    fun uploadPost(
        @Header("Authorization") authorization: String,
        @Part("data") data: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): Call<PostResponse>
}