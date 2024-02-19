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