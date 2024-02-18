package com.example.favoriteplace

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RallyDetailService {
    @GET("/pilgrimage/{rally_id}")
    fun getRallyDetail(
        @Path("rally_id") rallyId: Long,
        @Header("Authorization") authorization: String?
    ): Call<RallyDetailData>


}