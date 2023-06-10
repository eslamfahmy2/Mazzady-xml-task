package com.example.mazzadytask.networking

import com.example.mazzadytask.networking.dto.Categories
import com.example.mazzadytask.networking.dto.SubCategoryDto
import com.example.mazzadytask.networking.wrappers.WsResponseWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppServices {

    @GET("get_all_cats")
    suspend fun getAllCategories(): Response<WsResponseWrapper<Categories>>

    @GET("properties")
    suspend fun getSubCategories(@Query("cat") cat: Int): Response<WsResponseWrapper<List<SubCategoryDto>>>

    @GET("get-options-child/{option}")
    suspend fun getOptions(@Path("option") option: Int): Response<WsResponseWrapper<List<SubCategoryDto>>>

}