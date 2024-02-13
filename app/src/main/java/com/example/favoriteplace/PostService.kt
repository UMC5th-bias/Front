package com.example.favoriteplace

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {
    @Multipart
    @POST("/posts/free")
    fun uploadPost(
        @Header("Authorization") authorization: String,
        @Part("data") data: PostData,
        @Part images: List<MultipartBody.Part>
    ): Call<Void>
}