package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShopService {
    @GET("/shop/limited")
    fun getLimitedSales(
        @Header("Authorization") authorization: String?
    ): Call<SalesResponse>

    @GET("/shop/always")
    fun getUnlimitedSales(
        @Header("Authorization") authorization: String?
    ): Call<SalesResponse>

    @GET("/shop/detail/{item_id}")
    fun getItemDetails(
        @Header("Authorization") authorization: String?,
        @Path("item_id") itemId: Int
    ): Call<ItemDetails>

    @GET("/shop/new")
    fun getNewLimitedSales():Call<NewLimitedSalesResponse>

    @GET("/shop/new")
    fun getNewUnlimitedSales():Call<NewUnlimitedSalesResponse>

    @GET("/shop/detail/{item_id}")
    fun getDetailItem(
        @Header("Authorization") authorization:String?=null,
        @Path("item_id") itemId: Int
    ): Call<ShopDetailsResponse>

    @POST("/shop/purchase/{item_id}")
    fun purchaseItem(
        @Header("Authorization") token: String,
        @Path("item_id") itemId: Int
    ): Call<PurchaseResponse>

    // 아이템을 적용하는 PUT 요청을 보내는 메소드
    @PUT("/my/items/{item_id}")
    fun applyItem(
        @Header("Authorization") authorization: String,
        @Path("item_id") itemId: Int
    ): Call<ApplyResponse>
}