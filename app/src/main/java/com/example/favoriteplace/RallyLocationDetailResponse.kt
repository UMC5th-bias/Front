package com.example.favoriteplace

data class RallyLocationDetailComments(
    val page: Long,
    val size: Long,
    val comment: List<RallyLocationDetailComment>
)

data class RallyLocationDetailComment(
    val userInfo: RallyLocationDetailCommentUserInfo,
    val id: Long,
    val content: String,
    val passedTime: String,
    val isWrite: Boolean
)

data class RallyLocationDetailCommentUserInfo(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String,
    val profileTitleUrl: String,
    val profileIconUrl: String
)