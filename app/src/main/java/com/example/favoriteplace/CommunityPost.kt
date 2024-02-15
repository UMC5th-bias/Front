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

//data class CommunityComment(
//    val page: Int,
//    val size: Int,
//    val post: List<Comments>
//)

data class Comments(
    val id: Int,
    val content: String,
    val passedTime: String,
    val post: List<Posts>
)
