package com.apion.apionhome.data.model.dashboard

import android.os.Parcel
import android.os.Parcelable
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.data.model.community.Community
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class Dashboard(
    @SerializedName("feature")
    val feature: List<House>,
    @SerializedName("hanoi")
    val hanoiHouse: List<House>,
    @SerializedName("saigon")
    val saigonHouse: List<House>,

    @SerializedName("user_online")
    val userOnline: List<User_I>,
    @SerializedName("banner")
    val banners: List<Banner?>,
    @SerializedName("total_user")
    val totalUser: Int,
    @SerializedName("featured_community")
    val featureCommunity: List<House>,
    @SerializedName("communities")
    val communities: List<Community>
): Parcelable {

    fun getUserOnlineCount() = userOnline.size

}
