package com.apion.apionhome.data.model

import android.os.Parcelable
import com.apion.apionhome.utils.*
import com.apion.apionhome.utils.TimeFormat.TIME_FORMAT_API
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*


@Parcelize
data class House(
    @SerializedName("id")
    var id: Int = -1,
    @SerializedName("news_type")
    var newsType: String? = "For Sale",
    @SerializedName("houseType")
    var houseType: String?,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("title")
    var title: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("province")
    var province: String?,
    @SerializedName("district")
    var district: String?,
    @SerializedName("ward")
    var ward: String? = null,
    @SerializedName("street")
    var street: String? = null,
    @SerializedName("detail_address")
    var address: String? = null,
    @SerializedName("price")
    var price: Long? = 0,
    @SerializedName("frontWidth")
    var frontWidth: Double?,
    @SerializedName("acreage")
    var acreage: Double? = 0.0,
    @SerializedName("homeDirection")
    var homeDirection: String?,
    @SerializedName("bedrooms")
    var bedrooms: Int? = 0,
    @SerializedName("user_id")
    var userId: Int?,
    @SerializedName("seller_id")
    var sellerId: Int? = null,
    @SerializedName("owner")
    var owner: User_I? = null,
    @SerializedName("seller")
    var seller: User_I? = null,
    @SerializedName("images")
    var images: List<String>? = null,
    @SerializedName("related_houses")
    var relatedHouses: List<House>? = null,
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null,
    @SerializedName("commission_rate")
    var commissionRate: String? = "3",
) : GeneraEntity, Parcelable {

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is House && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is House && this == newItem

    operator fun compareTo(o: House): Int {
        var formater = SimpleDateFormat(
            TimeFormat.TIME_FORMAT_API,
            Locale.getDefault()
        )
        formater.timeZone = TimeZone.getDefault()
        var date = formater.parse(updatedAt)
        var date2 = formater.parse(o.updatedAt)
        return date.compareTo(date2)
    }

    fun getPriceConvert(): String {
        var tmpPrice = price ?: 0
        return when {
            tmpPrice / 1000000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000000.0
                return "${checkPrice(varue)} tỷ"
            }
            tmpPrice / 1000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000.0
                return "${checkPrice(varue)} triệu"
            }
            tmpPrice / 1000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000.0
                return "${checkPrice(varue)} nghìn"
            }
            else -> "$price đồng"
        }
    }

    fun getAcreageConvert(): String {
        var tmpPrice = acreage ?: 0.0
        return when {
            tmpPrice / 1000000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000000.0
                return "${checkPrice(varue)} tỷ"
            }
            tmpPrice / 1000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000.0
                return "${checkPrice(varue)} triệu"
            }
            tmpPrice / 1000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000.0
                return "${checkPrice(varue)} nghìn"
            }
            else -> "$acreage"
        }
    }

    fun getFrontWidthConvert(): String {
        var tmpPrice = frontWidth ?: 0.0
        return when {
            tmpPrice / 1000000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000000.0
                return "${checkPrice(varue)} tỷ"
            }
            tmpPrice / 1000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000.0
                return "${checkPrice(varue)} triệu"
            }
            tmpPrice / 1000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000.0
                return "${checkPrice(varue)} nghìn"
            }
            else -> "$frontWidth"
        }
    }

    fun getCommissionConvert(): String {
        var tmpPrice = (price ?: 0) * (commissionRate?.toInt() ?: 3) / 100
        if (status == "In Review") tmpPrice = 0
        return when {
            tmpPrice / 1000000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000000.0
                return "${checkPrice(varue)} tỷ"
            }
            tmpPrice / 1000000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000000.0
                return "${checkPrice(varue)} triệu"
            }
            tmpPrice / 1000.0 >= 1.0 -> {
                var varue = tmpPrice / 1000.0
                return "${checkPrice(varue)} nghìn"
            }
            else -> "$tmpPrice"
        }
    }

    fun getDefaultImage(): String? {
        return if (!images.isNullOrEmpty()) images?.get(0)
        else null
    }

    fun getDetailAddress(): String {
        var textAddress = ""
        address.let {
            if (address?.isNotEmpty() == true) {
                textAddress += address
                textAddress += ", "
            }
        }
        street.let {
            if (street?.isNotEmpty() == true) {
                textAddress += street
                textAddress += ", "
            }
        }

        ward.let {
            if (ward?.isNotEmpty() == true) {
                textAddress += ward
                textAddress += ", "
            }
        }
        district.let {
            if (district?.isNotEmpty() == true) {
                textAddress += district
                textAddress += ", "
            }
        }

        province.let {
            textAddress += province
        }

        return textAddress
    }

    fun getCreateDate(): String {
        var formater = SimpleDateFormat(
            TimeFormat.TIME_FORMAT_API,
            Locale.getDefault()
        )
        formater.timeZone = TimeZone.getDefault()
        var date = formater.parse(createdAt)
        return date?.let {
            date.toString(TimeFormat.DATE_FORMAT)
        } ?: ""
    }

    fun getDateAgo(): String {
        var timeUtc = createdAt?.toDate()
        var formater = SimpleDateFormat(
            TimeFormat.TIME_FORMAT_API,
            Locale.getDefault()
        )
        formater.timeZone = TimeZone.getDefault()
        var date = formater.parse(timeUtc?.formatTo(TIME_FORMAT_API))
        return date?.getRelativeTimeSpanString() ?: ""
    }

    fun isRedBack(): Boolean {
        return status == "Sold" || status == "Removed"
    }

    private fun checkPrice(number: Double): String {
        var priceString = String.format("%.2f", number)
        var priceTmp = if (priceString.split(",").last() == "00") priceString.split(",")
            .first() else priceString
        return if (priceTmp.split(",").last() == "00") priceTmp.split(",")
            .first() else priceTmp
    }
}