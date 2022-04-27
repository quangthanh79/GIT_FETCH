package com.apion.apionhome.data.model.local

import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.utils.removeSpecific
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open  class District(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("name")
    val name: String = "",
    @Expose(serialize = false, deserialize = false)
    var province: Province,
    @SerializedName("wards")
    val wards: MutableList<LocationName> = mutableListOf(),
    @SerializedName("streets")
    val streets: MutableList<LocationName> = mutableListOf(),
    @SerializedName("projects")
    val projects: MutableList<LocationProject> = mutableListOf(),
) : ILocation {

    fun copy(): District {
        return District(id, name, province)
    }

    override fun getTitle(): String = "${this.name}"

    override fun getContent(): String = this.name

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is District && this == newItem

    override fun toString(): String {
        return "$name${province.name}".removeSpecific()
    }
}
