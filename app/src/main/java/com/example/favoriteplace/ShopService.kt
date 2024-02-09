package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ShopService {
    @GET("/shop/limited")
    fun getLimitedSales(
        @Header("Authorization") authorization: String? = null
    ): Call<LimitedSalesResponse>

    @GET("/shop/always")
    fun getUnlimitedSales(
        @Header("Authorization") authorization: String? = null
    ): Call<UnlimitedSalesResponse>

    @GET("/shop/new")
    fun getNewLimitedSales():Call<NewLimitedSalesResponse>

    @GET("/shop/new")
    fun getNewUnlimitedSales():Call<NewUnlimitedSalesResponse>
}