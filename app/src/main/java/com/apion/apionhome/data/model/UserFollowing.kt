package com.apion.apionhome.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// đang theo dõi ai
@Parcelize
data class UserFollowing(
    @SerializedName("id")
    val id: Int?,
    //người theo dõi
    @SerializedName("follower_id")
    val followerId: String?,
    //người đang được theo dõi
    @SerializedName("being_followed_id")
    val beingFollowedId: String?,
    @SerializedName("being_followed")
    val beingFollowed: User_I?,
): Parcelable
