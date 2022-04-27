package com.apion.apionhome.data.model.local

import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.utils.removeSpecific
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationProject(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("name")
    val name: String = "",
    var district: District,
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lng")
    val lng: Double = 0.0,
) : ILocation {

    override fun getTitle(): String = district.getTitle()

    override fun getContent(): String = "$name, ${district.getContent()}"

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is LocationProject && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is LocationProject && this == newItem

    override fun toString(): String {
        return "${getContent()}${getTitle()}".removeSpecific()
    }
}
