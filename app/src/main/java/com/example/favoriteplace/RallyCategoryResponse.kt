package com.example.favoriteplace

import java.net.URL

data class RallyCategoryResponse (
    val id: String,
    val name: String,
    val pilgrimageNumber: Int,
    val myPilgrimageNumber: Int,
    val image: URL
)