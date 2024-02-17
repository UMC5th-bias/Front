package com.example.favoriteplace

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RallyCertifyService {

    data class PostData(
        val longitude: Double,
        val latitude: Double
    )


    data class RallyCertifyResponse(
        val success: Boolean? = null,
        val isComplete: Boolean? = null,
        val message: String
    )


    @POST("/pilgrimage/certify/{pilgrimage_id}")
    fun RallyCertify(
        @Path("pilgrimage_id") pilgrimageId: Long,
        @Header("Authorization") authorization: String,
        @Body data: PostData

        ): Call<RallyCertifyResponse>

}