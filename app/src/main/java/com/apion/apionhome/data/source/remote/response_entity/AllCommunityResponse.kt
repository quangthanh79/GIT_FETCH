package com.apion.apionhome.data.source.remote.response_entity

import com.apion.apionhome.data.model.community.Community
import com.google.gson.annotations.SerializedName

data class AllCommunityResponse(

    @SerializedName("data")
    val communities: List<Community>,

    @SerializedName("success")
    val isSuccess: Boolean,

    @SerializedName("message")
    val message: String,
)
