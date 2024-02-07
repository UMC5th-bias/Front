package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface AuthRetrofitInterface {

    //상점 신상품 페이지 api 연동 함수
    @GET("/shop/new")
    fun getNewItem():Call<ShopBannerNewFragment.JSON_data>
}