package com.example.favoriteplace

data class Region(
    val state: String,
    val detail: List<DistrictDetail>
)

data class DistrictDetail(
    val id: Int,
    val district: String
)