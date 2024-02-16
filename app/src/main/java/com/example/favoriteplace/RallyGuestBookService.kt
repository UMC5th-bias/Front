package com.example.favoriteplace

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RallyGuestBookService {

    data class GuestBookPost(
        val title: String,
        val content: String,
        val hashtags: List<String>
    )


    data class GuestbookResponse(
        val message: String,
        val success: Boolean? = null,
        val httpStatus: String? = null,
        val code: Int? = null
    )


    @Multipart
    @POST("/posts/guestbooks/{pilgrimage_id}")
    fun guestBookUploadPost(
        @Header("Authorization") authorization: String,
        @Part ("data") data: RequestBody,
        @Part files: List<MultipartBody.Part>,
        @Path("pilgrimageId") pilgrimageId: Long
    ): Call<GuestbookResponse>


}