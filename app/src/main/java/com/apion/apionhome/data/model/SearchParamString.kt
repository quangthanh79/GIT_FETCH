package com.apion.apionhome.data.model

import com.apion.apionhome.utils.ApiEndPoint.PART_ACREAGE_RANGE
import com.apion.apionhome.utils.ApiEndPoint.PART_DISTRICT
import com.apion.apionhome.utils.ApiEndPoint.PART_FRONT_WIDTH_RANGE
import com.apion.apionhome.utils.ApiEndPoint.PART_HOME_DIRECTION
import com.apion.apionhome.utils.ApiEndPoint.PART_NEWS_TYPE
import com.apion.apionhome.utils.ApiEndPoint.PART_PRICE_RANGE
import com.apion.apionhome.utils.ApiEndPoint.PART_PROVINCE
import com.apion.apionhome.utils.ApiEndPoint.PART_TITLE
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class SearchParamString(
    val province: String? = null,
    val district: String? = null,
    val priceRange: Range? = null,
    val acreageRange: Range? = null,
    val homeDirection: String? = null,
    val title: String? = null,
    @SerializedName("frontWidthRange")
    val frontWidthRange: String,
) {

    fun toJson(): String {
        val jsonObject = JsonObject().apply {
            province?.let {
                addProperty(PART_PROVINCE, it)
            }
            district?.let {
                addProperty(PART_DISTRICT, it)
            }
            priceRange?.let {
                addProperty(PART_PRICE_RANGE, it.toString())
            }
            acreageRange?.let {
                addProperty(PART_ACREAGE_RANGE, it.toString())
            }
            homeDirection?.let {
                addProperty(PART_HOME_DIRECTION, it)
            }
            title?.let {
                addProperty(PART_TITLE, it)
            }
            frontWidthRange?.let {
                addProperty(PART_FRONT_WIDTH_RANGE, it.toString())
            }
        }

        return jsonObject.toString()
    }
}
