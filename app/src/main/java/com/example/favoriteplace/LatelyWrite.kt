package com.example.favoriteplace

import android.icu.text.CaseMap.Title

data class LatelyWrite(
    var title: String?="",
    var writer: String?="",
    var eye: Int?=0,
    var like: Int?=0,
    var clock: String?="",
    var comment_num: Int?=0
)