package com.apion.apionhome.data.model.local

import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.utils.removeSpecific
import com.google.gson.annotations.SerializedName

data class Province(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("code")
    val code: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("districts")
    val districts: MutableList<District>,
) : ILocation {

    fun copy(): Province {
        return Province(id, code, name, mutableListOf())
    }

    override fun getTitle(): String = name

    override fun getContent(): String = "${name} (${code})"

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this == newItem

    override fun toString(): String {
        return "$code $name".removeSpecific()
    }
}
