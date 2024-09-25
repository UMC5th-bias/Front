package com.example.favoriteplace

data class UnlimitedSalesResponse(
    val isLoggedIn: Boolean,
    val userInfo: Any?, // 사용자 정보에 대한 더 구체적인 모델이 필요할 수 있음
    val titles: List<UnlimitedCategory>,
    val icons: List<UnlimitedCategory>
)


data class UnlimitedCategory(
    val category: String,
    val itemList: List<UnlimitedItem>
)

data class UnlimitedItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val point: Int
)