package com.example.favoriteplace

import androidx.annotation.DrawableRes

data class BannerItem(
    @DrawableRes val imageRes: Int,
    val showButton: Boolean
)
