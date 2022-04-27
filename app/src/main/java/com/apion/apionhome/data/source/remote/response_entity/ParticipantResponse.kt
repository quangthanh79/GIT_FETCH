package com.apion.apionhome.data.source.remote.response_entity

import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.data.model.community.Participant
import com.google.gson.annotations.SerializedName

data class ParticipantResponse(
    @SerializedName("success")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val participant: Participant,
)
