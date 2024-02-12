package com.example.favoriteplace

data class NewLimitedSalesResponse(
    val titles: List<NewLimitedCategory>,
    val icons: List<NewLimitedCategory>
)

data class NewLimitedCategory(
    val status: String,
    val itemList: List<NewLimitedItem>
)

data class NewLimitedItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val point: Int
)
