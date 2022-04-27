package com.apion.apionhome.data.model

import com.google.gson.annotations.SerializedName

data class SellHouseRequest(
    @SerializedName("seller_phone")
    val seller_phone: String,
)
