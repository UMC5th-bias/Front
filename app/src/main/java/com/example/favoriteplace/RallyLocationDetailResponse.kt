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

data class RallyLocationDetailStatusUpdate(
    val certifyButtonEnabled: Boolean, // 인증하기 가능 여부
    val guestbookButtonEnabled: Boolean, // 인증글 작성 가능 여부
    val multiGuestbookButtonEnabled: Boolean // 다회차 인증글 작성 가능 여부
)