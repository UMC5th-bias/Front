package com.example.favoriteplace

data class NewUnlimitedSalesResponse(
    val titles: List<NewLimitedCategory>,
    val icons: List<NewLimitedCategory>
)

data class NewUnlimitedCategory(
    val status: String,
    val itemList: List<NewLimitedItem>
)

data class NewUnlimitedItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val point: Int
)
