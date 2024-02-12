package com.example.favoriteplace

import java.net.URL

data class RallyCategoryResponse (
    val id: Long,
    val name: String,
    val pilgrimageNumber: Int,
    val myPilgrimageNumber: Int,
    val image: URL
)