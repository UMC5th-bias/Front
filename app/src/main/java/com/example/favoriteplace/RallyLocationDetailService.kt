package com.example.favoriteplace

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RallyLocationDetailService {

    data class RallyInfo(
        @SerializedName("rallyName") val rallyName: String,
        @SerializedName("pilgrimageNumber") val pilgrimageNumber: Int,
        @SerializedName("myPilgrimageNumber") val myPilgrimageNumber: Int,
        @SerializedName("image") val image: String,
        @SerializedName("realImage") val realImage: String,
        @SerializedName("address") val address: String,
        @SerializedName("latitude") val latitude: Double,
        @SerializedName("longitude") val longitude: Double,
        @SerializedName("isCertified") val isCertified: Boolean,
        @SerializedName("isWritable") val isWritable: Boolean,
        @SerializedName("isMultiWritable") val isMultiWritable: Boolean
    )

    @GET("/pilgrimage/detail/{pilgrimageId}")
    fun getRallyInfo(): Call<RallyInfo>
}