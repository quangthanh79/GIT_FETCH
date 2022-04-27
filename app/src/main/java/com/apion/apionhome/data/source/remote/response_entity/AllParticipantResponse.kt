package com.apion.apionhome.data.source.remote.response_entity

import com.apion.apionhome.data.model.community.Participant
import com.google.gson.annotations.SerializedName

data class AllParticipantResponse(

    @SerializedName("data")
    val participants: List<Participant>,

    @SerializedName("success")
    val isSuccess: Boolean,

    @SerializedName("message")
    val message: String,
)
