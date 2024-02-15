package com.example.favoriteplace

import com.google.gson.annotations.SerializedName
import java.net.URL

data class Region(
    val state: String,
    val detail: List<DistrictDetail>
)

data class DistrictDetail(
    val id: Long,
    val district: String
)

data class  RallyPlaceAnimation(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("detailAddress") val detailAddress: String,
    @SerializedName("image") val image: URL,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String
)