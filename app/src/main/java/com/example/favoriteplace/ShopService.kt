package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ShopService {
    @GET("/shop/limited")
    fun getLimitedSales(
        @Header("Authorization") authorization: String?
    ): Call<LimitedSalesResponse>

    @GET("/shop/always")
    fun getUnlimitedSales(
        @Header("Authorization") authorization: String?
    ): Call<UnlimitedSalesResponse>

    @GET("/shop/detail/{item_id}")
    fun getItemDetails(@Path("item_id") itemId: Int): Call<ItemDetails>

}