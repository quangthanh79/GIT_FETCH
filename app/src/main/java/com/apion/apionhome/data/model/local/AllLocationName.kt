package com.apion.apionhome.data.model.local

import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.utils.removeSpecific
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllLocationName() : LocationName(district = District(province = Province(districts = mutableListOf()))) {

    override fun getTitle(): String = "Tất cả vị trí"

    override fun getContent(): String = "Toàn bộ vị trí dưới đây"

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this == newItem

    override fun toString(): String {
        return "${getContent()}${getTitle()}".removeSpecific()
    }
}
