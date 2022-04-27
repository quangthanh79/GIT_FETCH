package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.House
import com.apion.apionhome.data.model.User_I
import com.apion.apionhome.databinding.ItemPostCommunityBinding
import com.apion.apionhome.databinding.ItemUserOnlineBinding

class PostCommunityAdapter(private val listener: (House) -> Unit) :
    BaseAdapter<House, ItemPostCommunityBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<House, ItemPostCommunityBinding> =
        PostCommunityViewHolder(
            ItemPostCommunityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )

    class PostCommunityViewHolder(
        private val itemPostCommunityBinding: ItemPostCommunityBinding,
        listener: (House) -> Unit
    ) : BaseViewHolder<House, ItemPostCommunityBinding>(itemPostCommunityBinding, listener) {
        override fun onBind(itemData: House) {
            super.onBind(itemData)
            itemPostCommunityBinding.house = itemData
        }
    }
}
