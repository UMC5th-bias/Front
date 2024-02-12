package com.example.favoriteplace

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
    val id: Long,
    val title: String,
    val detailAddress: String,
    val image: URL,
    val latitude: String,
    val longitude: String
)