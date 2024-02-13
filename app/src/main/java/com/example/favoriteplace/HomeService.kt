package com.example.favoriteplace


import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Header
import java.net.URL

interface HomeService {


    data class LoginResponse(
        @SerializedName("isLoggedIn") val isLoggedIn: Boolean,
        @SerializedName("userInfo") val userInfo: UserInfo,
        @SerializedName("rally") val rally: Rally,
        @SerializedName("trendingPosts") val trendingPosts: List<TrendingPosts>
    )


    data class UserInfo(
        val id: Int,
        val nickname: String,
        val profileImageUrl: String?,
        val profileTitleUrl: String?,
        val profileIconUrl: String?
    )

    data class Rally(
        val id: Int,
        val name: String,
        val backgroundImageUrl: String?,
        val pilgrimageNumber: Int,
        val completeNumber: Int
    )

    data class TrendingPosts(
        val id: Int,
        val rank: Int,
        val title: String,
        val profileImageUrl: String?,
        val profileIconUrl: String?,
        val hashtags:  List<String>?,
        val passedTime: String,
        val board: String
    )

    @GET("/home")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String?
    ): Response<LoginResponse>
}