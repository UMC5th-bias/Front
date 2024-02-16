package com.example.favoriteplace

data class CommunityPost(
    val page: Int,
    val size: Int,
    val post: List<Posts>
)

data class Posts(
    val id: Int,
    val title: String,
    val nickname: String,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val passedTime: String
)

data class CommunityComment(
    val page: Int,
    val size: Int,
    val comment: List<Comments>
)

data class Comments(
    val id: Int,
    val content: String,
    val passedTime: String,
    val post: Posts
)

data class RallyPost(
    val page: Int,
    val size: Int,
    val guestBook: List<GuestBook>
)

data class GuestBook(
    val id: Int,
    val title: String,
    val nickname: String,
    val thumbnail: String?,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val passedTime: String,
    val hashTags: List<String>
)

data class HashTag(
    val hashTag: String
)