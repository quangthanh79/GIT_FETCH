package com.apion.apionhome.data.model.local

import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.utils.removeSpecific
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class LocationName(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("name")
    val name: String = "",
    var district: District,
    @SerializedName("prefix")
    val prefix: String = ""
) : ILocation {

    override fun getTitle(): String = "$prefix $name"

    override fun getContent(): String = "$name"

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this == newItem

    override fun toString(): String {
        return "${getContent()}${getTitle()}".removeSpecific()
    }

    fun getNameLocation() = "$prefix $name"
}
