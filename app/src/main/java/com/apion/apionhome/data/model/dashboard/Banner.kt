package com.apion.apionhome.data.model.dashboard

import android.os.Parcelable
import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.data.model.House
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
) : GeneraEntity, Parcelable{

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is Banner && this.image == newItem.image

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is Banner && this == newItem
}
