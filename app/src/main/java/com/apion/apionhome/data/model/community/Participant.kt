package com.apion.apionhome.data.model.community

import android.os.Parcelable
import com.apion.apionhome.data.model.GeneraEntity
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.User_I
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participant(
    @SerializedName("id")
    val id: Int,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("user")
    val user: User_I,
    @SerializedName("community")
    val community: Community,
) : GeneraEntity, Parcelable {

    override fun areItemsTheSame(newItem: GeneraEntity): Boolean =
        newItem is Participant && this.id == newItem.id

    override fun areContentsTheSame(newItem: GeneraEntity): Boolean =
        newItem is Participant && this == newItem
}
