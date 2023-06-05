package com.example.mazzadytask.networking.dto

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("children")
    val children: List<Children>? = null,
    @SerializedName("circle_icon")
    val circle_icon: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("disable_shipping")
    val disable_shipping: Int? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("slug")
    val slug: String? = null
)