package com.example.mazzadytask.networking.dto

import com.google.gson.annotations.SerializedName

data class OptionsDto(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("list")
    val list: Boolean?,
    @SerializedName("name")
    val name: String,
    @SerializedName("options")
    val options: List<Option>?,
    @SerializedName("other_value")
    val other_value: String?,
    @SerializedName("parent")
    val parent: Int?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("value")
    val value: String?
)