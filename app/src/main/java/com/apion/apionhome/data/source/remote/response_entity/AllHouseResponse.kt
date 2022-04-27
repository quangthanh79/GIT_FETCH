package com.apion.apionhome.data.source.remote.response_entity

import com.apion.apionhome.data.model.House
import com.google.gson.annotations.SerializedName

data class AllHouseResponse(

    @SerializedName("data")
    val houses: List<House>,

    @SerializedName("success")
    val isSuccess: Boolean,

    @SerializedName("message")
    val message: String,
)
