package com.apion.apionhome.data.model.local

import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.utils.removeSpecific
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllDistrict : District(province=Province(-1, "","", mutableListOf())) {


    override fun getTitle(): String = "Tất cả vị trí"

    override fun getContent(): String = "Toàn bộ vị trí dưới đây"

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is AllDistrict && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is AllDistrict && this == newItem

    override fun toString(): String {
        return "$name${province.name}".removeSpecific()
    }
}
