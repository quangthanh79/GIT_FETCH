package com.apion.apionhome.data.source.remote.response_entity

import com.apion.apionhome.data.model.User_I
import com.google.gson.annotations.SerializedName

data class UserResponse_I(
    @SerializedName("success")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val user: User_I
)
