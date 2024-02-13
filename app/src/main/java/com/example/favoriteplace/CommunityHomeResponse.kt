package com.example.favoriteplace

data class CommunityHomeTrendingFree (
    val date: String,
    val rank: List<CommunityHomeTrendingFreeUnit>
)

data class CommunityHomeTrendingFreeUnit (
    val id: Long,
    val title: String
)

data class CommunityHomeTrendingGuestbook (
    val date: String,
    val rank: List<CommunityHomeTrendingGuestbookUnit>
)

data class CommunityHomeTrendingGuestbookUnit (
    val id: Long,
    val title: String
)