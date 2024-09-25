package com.example.favoriteplace

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface SignUpService {


    data class SignUpRequest(
        @SerializedName("nickname") val nickname: String,
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
        @SerializedName("snsAllow") val snsAllow: Boolean,
        @SerializedName("introduction") val introduction: String
    )



    data class SignUpResponse(
        @SerializedName("nickname") val nickname: String,
        @SerializedName("introduction") val introduction: String,
        @SerializedName("profileImage") val profileImage: String,
        @SerializedName("profileTitleItem") val profileTitleItem: String
    )


    @Multipart
    @POST("/auth/signup")
    fun signUp(
        @Part("data") data: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<SignUpResponse>


}
