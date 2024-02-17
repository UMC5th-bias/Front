package com.example.favoriteplace

data class PostData(
    val title: String,
    val content: String
)

data class PostResponse(
    val message: String,             // 성공 또는 실패 메시지
    val success: Boolean? = null,  // 성공 여부, 실패 응답에서만 존재
    val httpStatus: String? = null, // HTTP 상태, 실패 응답에서만 존재
    val code: Int? = null          // 오류 코드, 실패 응답에서만 존재할 수 있음
)
