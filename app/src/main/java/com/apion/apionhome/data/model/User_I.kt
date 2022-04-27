package com.apion.apionhome.data.model

import android.os.Parcelable
import com.apion.apionhome.data.model.community.Participant
import com.apion.apionhome.utils.TimeFormat
import com.apion.apionhome.utils.toString
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class User_I(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("referal")
    var refer: String? = null,
    @SerializedName("dob")
    var dateOfBirth: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("province")
    var province: String?,
    @SerializedName("district")
    var district: String?,
    @SerializedName("ward")
    var ward: String? = null,
    @SerializedName("street")
    var street: String? = null,

    @SerializedName("avatar")
    val avatar: String? = null,


    @SerializedName("sex")
    var sex: String? = null,
    @SerializedName("academicLevel")
    val academicLevel: String? = null,
    @SerializedName("job")
    val job: String? = null,

    @SerializedName("isFirst")
    val isFirst: Int? = null,
    @SerializedName("position")
    val position: String? = null,
    @SerializedName("permission")
    val permission: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("facebook_id")
    var facebook_id: String? = null,
    @SerializedName("bios")
    var bios: String? = null,
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("employee_id")
    val employeeId: Int? = null,
    @SerializedName("boss_id")
    val bossId: Int? = null,
    @SerializedName("community_id")
    val communityId: Int? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("created_at")
    val created_at: String? = null,
    @SerializedName("updated_at")
    val updated_at: String? = null,
    @SerializedName("my_houses_count")
    val myHousesCount: Int? = null,
    @SerializedName("my_staff_count")
    val myStaffCount: Int? = null,
    @SerializedName("house_sold_count")
    val houseSoldCount: Int? = null,
    @SerializedName("houses_bookmark_count")
    val houseBookmarkCount: Int? = null,

    @SerializedName("participants_count")
    val participantsCount: Int? = null,
    @SerializedName("following_count")
    val followingCount: Int? = null,
    @SerializedName("followed_count")
    val followedCount: Int? = null,

    @SerializedName("boss")
    val boss: User_I? = null,
    @SerializedName("my_houses")
    val myHouses: List<House>? = null,
    @SerializedName("my_staff")
    val myStaff: List<User_I>? = null,
    @SerializedName("house_sold")
    val houseSold: List<House>? = null,
    @SerializedName("houses_bookmark")
    val bookmarks: List<BookMark>? = null,
    @SerializedName("participants")
    val participants: List<Participant>? = null,
    @SerializedName("following")
    val following: List<UserFollowing>? = null,
    @SerializedName("followed")
    val followed: List<UserFollowed>? = null,













    ) : GeneraEntity, Parcelable {

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is User_I && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is User_I && this == newItem

    fun isFollowing(userId: String): Boolean {
        var isFollow = false
        following?.let {
            for (element in it) {
                if (element.beingFollowedId == userId) {
                    isFollow = true
                    break
                }
            }
        }
        return isFollow
    }

    fun getPositionPermission(): String {
        val textPer = if (permission == "Host Side") {
            "đầu chủ"
        } else {
            "đầu khách"
        }
        val textPos = when (position) {
            "Director" -> {
                "Giám đốc"
            }
            "Earl" -> {
                "Bá tước"
            }
            "Manager" -> {
                "Trưởng phòng"
            }
            "Leader" -> {
                "Trưởng nhóm"
            }
            "Captain" -> {
                "Đội trưởng"
            }
            "Expert" -> {
                "Chuyên viên"
            }
            else -> {
                "Đang bán"
            }
        }
        return "$textPos $textPer"
    }

    fun isBookmark(houseId: Int): Boolean {
        bookmarks?.forEach {
            if (it.house?.id == houseId) return true
        }
        return false
    }

    fun getDOBDate(): String {
        val date = SimpleDateFormat(
            TimeFormat.TIME_FORMAT_API_1,
            Locale.getDefault()
        ).parse(dateOfBirth)
        return date?.let {
            date.toString(TimeFormat.DATE_FORMAT)
        } ?: ""
    }

    fun getHouseBookmark(): List<House?>? {
        return bookmarks?.map {
            it.house
        }
    }

    fun getSexString(): String {
        return if (sex == "Male") "Nam" else "Nữ"
    }

    fun getHouseCreate():List<House>? {
        return myHouses?.filter {
            it.status != "Sold" && it.status != "Removed"
        }
    }

    fun getNameString(): String {
        return if (name.isNullOrBlank()) "Chưa cập nhật" else name!!
    }

}
