package com.apion.apionhome.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// đang được ai theo dõi
@Parcelize
data class UserFollowed(
    @SerializedName("id")
    val id: Int?,
    //người đang được  theo dõi
    @SerializedName("being_followed_id")
    val beingFollowedId: String?,
    //người theo dõi
    @SerializedName("follower_id")
    val followerId: String?,
    @SerializedName("follower")
    val follower: User_I?,
): Parcelable