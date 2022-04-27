package com.apion.apionhome.data.source.remote.response_entity

import com.apion.apionhome.data.model.House
import com.google.gson.annotations.SerializedName

data class HouseResponse(
    @SerializedName("success")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val house: House
)
