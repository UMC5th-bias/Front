package com.example.favoriteplace

import java.net.URL

data class RallyDetailReponse (
    val name: String,
    val pilgrimageNumber: Int,
    val myPilgrimageNumber: Int,
    val image: URL,
    val description: String,
    val achieveNumber: String,
    val itemImage: URL,
    val isLike: Boolean
)