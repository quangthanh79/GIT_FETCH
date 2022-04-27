package com.apion.apionhome.data.model

import com.google.gson.annotations.SerializedName

data class UserFollowRequest(
    @SerializedName("follower_id")
    val followerId: Int,
    @SerializedName("being_followed_id")
    val beingFollowedId: Int
)
