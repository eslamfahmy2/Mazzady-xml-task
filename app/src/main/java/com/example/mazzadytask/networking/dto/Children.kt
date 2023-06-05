package com.example.mazzadytask.networking.dto

import com.google.gson.annotations.SerializedName

data class Children(
    @SerializedName("children")
    val children: String?,
    @SerializedName("circle_icon")
    val circle_icon: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("disable_shipping")
    val disable_shipping: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("slug")
    val slug: String?
)