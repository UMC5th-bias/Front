package com.example.favoriteplace

import com.google.gson.annotations.SerializedName


data class RallyDetailData(
    @SerializedName(value = "name") var name:String,
    @SerializedName(value = "pilgrimageNumber") var pilgrimageNumber:Int,
    @SerializedName(value = "myPilgrimageNumber") var myPilgrimageNumber:Int,
    @SerializedName(value = "image") var image : String,
    @SerializedName(value = "description") var description : String,
    @SerializedName(value = "achieveNumber") var achieveNumber:Int,
    @SerializedName(value = "itemImage") var itemImage: String,
    @SerializedName(value = "isLike") var isLike:Boolean
)


data class UpdateLikeResponse(
    val success: Boolean,
    val message: String
)