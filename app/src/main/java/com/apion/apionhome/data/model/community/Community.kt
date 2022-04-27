package com.apion.apionhome.data.model.community

import android.os.Parcelable
import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.data.model.House
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Community(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("name")
    val name: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("short_description")
    val shortDesc: String,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("cover")
    val cover: String? = null,
    @SerializedName("created_at")
    val created_at: String? = null,
    @SerializedName("updated_at")
    val updated_at: String? = null,
    @SerializedName("houses")
    val houses: List<House> = emptyList(),
    @SerializedName("participants")
    val participants: List<Participant> = emptyList(),
) : GeneraEntity, Parcelable {

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is Community && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is Community && this == newItem
}
