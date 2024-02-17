package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityAPIService {
    @GET("posts/free?")
    fun getPosts(
        @Header("Authorization") authorization:String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Call<CommunityPost>

    @GET("posts/free/my-posts")
    fun getMyPosts(
        @Header("Authorization") authorization:String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<CommunityPost>

    @GET("posts/free/my-comments")
    fun getMyComments(
        @Header("Authorization") authorization:String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<CommunityComment>

    @GET("posts/guestbooks?")
    fun getRallyPost(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Call<RallyPost>

    @GET("posts/guestbooks/my-posts")
    fun getMyRally(
        @Header("Authorization") authorization:String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<RallyMy>

    @GET("posts/guestbooks/my-comments")
    fun getMyRallyComment(
        @Header("Authorization") authorization:String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<RallyMyComment>

    @GET("/posts/free/{post_id}")
    fun getFreePostDetail(
        @Path("post_id") postId: Int
    ): Call<PostDetailResponse>

    @GET("/posts/free/{post_id}/comments")
    fun getFreeCommentDetail(
        @Path("post_id") postId: Long
    ): Call<FreeCommentDetailResponse>

}