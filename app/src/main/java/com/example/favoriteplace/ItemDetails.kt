package com.example.favoriteplace

data class ItemDetails(
    val userPoint: Int,
    val category: String,
    val imageUrl: String,
    val saleDeadline: String?, // null 가능성이 있으므로 String? 타입을 사용
    val status: String,
    val name: String,
    val point: Int,
    val description: String?, // null 가능성이 있으므로 String? 타입을 사용
    val alreadyBought: Boolean
)
