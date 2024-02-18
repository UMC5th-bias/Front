package com.example.favoriteplace

import com.google.gson.annotations.SerializedName

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

data class RallyMy(
    val page: Int,
    val size: Int,
    val myGuestBookInfo: List<GuestMy>
)

data class GuestMy(
    val id: Int,
    val title: String,
    val nickname: String,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val passedTime: String,
)

data class RallyMyComment(
    val page: Int,
    val size: Int,
    val comment: List<MyComments>
)

data class MyComments(
    val id: Int,
    val content: String,
    val passedTime: String,
    val myGuestBookInfo: GuestMy
)

data class PostDetailResponse(
    @SerializedName("userInfo") val userInfo: PostUserInfo,
    @SerializedName("postInfo") val postInfo: PostInfo
)

data class PostUserInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String,
    @SerializedName("profileTitleUrl") val profileTitleUrl: String,
    @SerializedName("profileIconUrl") val profileIconUrl: String
)

data class PostInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("views") val views: Int,
    @SerializedName("likes") val likes: Int,
    @SerializedName("comments") val comments: Int,
    @SerializedName("isLike") val isLike: Boolean,
    @SerializedName("isWrite") val isWrite: Boolean,
    @SerializedName("passedTime") val passedTime: String,
    @SerializedName("image") val image: List<String>
)

data class FreeCommentDetailResponse(
    val page: Int,
    val size: Int,
    val comment: List<Comment>
)

data class Comment(
    val userInfo: PostUserInfo,
    val id: Int,
    val content: String,
    val passedTime: String,
    val isWrite: Boolean
)

data class Pilgrimage(
    val name: String,
    val pilgrimageNumber: Int,
    val completeNumber: Int,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageAnime: String,
    val imageReal: String
)

data class RallyGuestBook(
    val id: Long,
    val title: String,
    val content: String,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val isLike: Boolean,
    val isWrite: Boolean,
    val passedTime: String,
    val image: List<String>,
    val hashTag: List<String>
)

data class RallyDetailResponse(
    val userInfo: PostUserInfo,
    val pilgrimage: Pilgrimage,
    val guestBook: RallyGuestBook
)
