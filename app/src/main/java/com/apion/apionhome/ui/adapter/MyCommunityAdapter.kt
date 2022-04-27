package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.databinding.ItemMyCommunityBinding
import com.apion.apionhome.utils.setWith

class MyCommunityAdapter(
    private val listener: (Community) -> Unit,
) :
    BaseAdapter<Community, ItemMyCommunityBinding>() {

    override fun getItemCount(): Int {
        return if (super.getItemCount() < 8) {
            super.getItemCount()
        } else {
            8
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Community, ItemMyCommunityBinding> {
        val view = ItemMyCommunityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyCommunityViewHolder(
            view,
            listener,
        )
    }

    class MyCommunityViewHolder(
        private val itemCommunityBinding: ItemMyCommunityBinding,
        listener: (Community) -> Unit,
    ) : BaseViewHolder<Community, ItemMyCommunityBinding>(itemCommunityBinding, listener) {
        override fun onBind(itemData: Community) {
            super.onBind(itemData)
            println(itemData)
            itemCommunityBinding.community = itemData
        }
    }
}
