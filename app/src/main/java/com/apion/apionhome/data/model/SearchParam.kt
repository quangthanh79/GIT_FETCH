package com.apion.apionhome.data.model

import com.google.gson.annotations.SerializedName

data class SearchParam(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("province")
    val province: String? = null,
    @SerializedName("district")
    val district: String? = null,
    @SerializedName("ward")
    val ward: String? = null,
    @SerializedName("street")
    val street: String? = null,
    @SerializedName("houseType")
    val houseType: String? = null,
    @SerializedName("homeDirection")
    val homeDirection: String? = null,
    @SerializedName("priceRange")
    val priceRange: String? = null,
    @SerializedName("acreageRange")
    val acreageRange: String? = null,
    @SerializedName("frontWidthRange")
    val frontWidthRange: String?,
    @SerializedName("bedrooms")
    val bedRoom: Int? = 0,
)
