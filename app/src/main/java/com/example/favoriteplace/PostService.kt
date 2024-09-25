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
        @Part ("data") data: RequestBody, // JSON 형식의 데이터를 담은 RequestBody
        @Part files: List<MultipartBody.Part> // 파일 데이터
    ): Call<PostResponse>

}