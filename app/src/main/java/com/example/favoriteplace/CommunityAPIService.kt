package com.example.favoriteplace

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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
        @Header("Authorization") authorization:String?,
        @Path("post_id") postId: Int
    ): Call<PostDetailResponse>

    @GET("/posts/free/{post_id}/comments")
    fun getFreeCommentDetail(
        @Header("Authorization") authorization:String?,
        @Path("post_id") postId: Long
    ): Call<FreeCommentDetailResponse>

    @POST("/posts/free/{post_id}/comments")
    fun postFreeComment(
        @Header("Authorization") authorization:String?,
        @Path("post_id") postId: Long,
        @Body requestBody: RequestBody
    ): Call<ApplyResponse>

    @GET("posts/free/search")
    fun getFreeSearch(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("searchType") searchType: String,
        @Query("keyword") keyword: String
    ): Call<CommunityPost>

    @GET("posts/guestbooks/search")
    fun getRallySearch(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("searchType") searchType: String,
        @Query("keyword") keyword: String
    ): Call<RallyPost>

    @GET("/posts/guestbooks/{guestbook_id}")
    fun getRallyPostDetail(
        @Header("Authorization") authorization:String?,
        @Path("guestbook_id") guestBookId: Long,
    ): Call<RallyDetailResponse>

    @POST("/posts/guestbooks/{guestbook_id}/comments")
    fun postRallyComment(
        @Header("Authorization") authorization:String?,
        @Path("guestbook_id") guestBookId: Long,
        @Body requestBody: RequestBody
    ): Call<ApplyResponse>

    @PUT("posts/free/{post_id}/like")
    fun sendFreeLike(
        @Header("Authorization") authorization:String?,
        @Path("post_id") postId: Int,
    ): Call<PostDetail>

    @POST("posts/guestbooks/{guestbook_id}/like")
    fun sendRallyLike(
        @Header("Authorization") authorization:String?,
        @Path("guestbook_id") guestbookId: Long,
    ): Call<PostDetail>
}