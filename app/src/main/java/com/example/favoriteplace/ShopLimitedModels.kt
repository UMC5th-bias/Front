package com.example.favoriteplace

data class LimitedSalesResponse(
    val isLoggedIn: Boolean,
    val userInfo: Any?, // 사용자 정보에 대한 더 구체적인 모델이 필요할 수 있음
    val titles: List<LimitedCategory>,
    val icons: List<LimitedCategory>
)

data class LimitedCategory(
    val category: String,
    val itemList: List<LimitedItem>
)

data class LimitedItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val point: Int
)