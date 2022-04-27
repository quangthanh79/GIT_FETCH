package com.apion.apionhome.data.source.remote.response_entity

import com.apion.apionhome.data.model.BookMark
import com.google.gson.annotations.SerializedName

data class BookMarkResponse(
    @SerializedName("success")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val bookMark: BookMark
)
