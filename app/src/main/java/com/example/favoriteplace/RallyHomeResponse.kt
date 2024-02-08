package com.example.favoriteplace

import java.net.URL

data class RallyHomeTrending(
    val id: Long,
    val name: String,
    val pilgrimageNumber: Int,
    val myPilgrimageNumber: Int,
    val image: URL
)

data class RallyHomeResponseMyRally(
    val likedRallySize: Int,
    val likedRally: List<RallyHomeLikedRally>,
    val guestBookSize: Int,
    val guestBook: List<RallyHomeGuestBook>
)

data class RallyHomeLikedRally(
    val id: Int,
    val name: String,
    val image: URL
)

data class RallyHomeGuestBook(
    val id: Int,
    val title: String,
    val createdAt: String,
    val image: URL,
    val hashTag: List<String>
)