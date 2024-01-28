package com.example.favoriteplace
data class CertifiedRallyItem(
    val title: String,      // 인증글 제목
    val tag1: String,       // 인증글 태그 1
    val tag2: String,       // 인증글 태그 2
    val imageResId: Int,    // 랠리 이미지의 리소스 ID
    val time: String        // 인증글 작성 시간
)
