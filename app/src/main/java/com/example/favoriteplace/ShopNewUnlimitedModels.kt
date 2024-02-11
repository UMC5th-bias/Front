package com.example.favoriteplace

data class NewUnlimitedSalesResponse(
    val titles: List<NewUnlimitedCategory>,
    val icons: List<NewUnlimitedCategory>
)

data class NewUnlimitedCategory(
    val status: String,
    val itemList: List<NewUnlimitedItem>
)

data class NewUnlimitedItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val point: Int
)
