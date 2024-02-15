package com.example.favoriteplace

data class SalesResponse(
    val isLoggedIn: Boolean,
    val userInfo: UserInfo?, // 사용자 정보에 대한 더 구체적인 모델이 필요할 수 있음
    val titles: List<LimitedCategory>,
    val icons: List<LimitedCategory>
)
data class UserInfo(
    val id: Int,
    val nickname: String,
    val point: Int,
    val profileImageUrl: String?,
    val profileTitleUrl: String,
    val profileIconUrl: String?
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