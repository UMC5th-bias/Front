package com.example.favoriteplace

import java.net.URL

data class MyInfo(
    val doneRally: Long,
    val visitedPlace: Long,
    val posts: Long,
    val comments: Long
)

data class MyProfile(
    val nickname: String?,
    val introducation: String,
    val point: Long,
    val profileImg: String?,
    val userTitleImg: String?,
    val userIconImg: String?,
    val email: String?
)

data class MyItems(
    val limited: List<MyItem>,
    val always: List<MyItem>,
    val prilgrimage: List<MyItem>
)

data class MyItem(
    val id: Long,
    val imageUrl: String,
    val isWear: Boolean
)