package com.example.mazzadytask.networking.dto

import com.google.gson.annotations.SerializedName

data class CategoriesDto(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val data: Categories?,
    @SerializedName("msg")
    val msg: String?
)

data class Categories(
    @SerializedName("ads_banners")
    val ads_banners: List<AdsBanner>?,
    @SerializedName("categories")
    val categories: List<Category>?,
    @SerializedName("google_version")
    val google_version: String?,
    @SerializedName("huawei_version")
    val huawei_version: String?,
    @SerializedName("ios_version")
    val ios_version: String?,
)