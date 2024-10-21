package com.example.favoriteplace

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment

data class BannerItemV2(
    @DrawableRes val imageRes: Int, // 배너 이미지 리소스
    val fragmentClassName: String? // 프래그먼트의 클래스 이름을 넘김 (null 가능)
)
