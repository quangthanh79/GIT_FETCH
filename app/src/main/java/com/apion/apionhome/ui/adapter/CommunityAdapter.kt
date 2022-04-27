package com.apion.apionhome.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.apion.apionhome.R
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.community.Community
import com.apion.apionhome.databinding.ItemCommunityBinding

class CommunityAdapter(
    private val listener: (Community) -> Unit,
    private val onClickButtonJoin: (Boolean) -> Unit
) :
    BaseAdapter<Community, ItemCommunityBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Community, ItemCommunityBinding> =
        CommunityViewHolder(
            parent.context,
            ItemCommunityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener,
            onClickButtonJoin
        )

    class CommunityViewHolder(
        private val context: Context,
        private val itemCommunityBinding: ItemCommunityBinding,
        listener: (Community) -> Unit,
        private val onClickButtonJoin: (Boolean) -> Unit
    ) : BaseViewHolder<Community, ItemCommunityBinding>(itemCommunityBinding, listener) {
        override fun onBind(itemData: Community) {
            super.onBind(itemData)
            itemCommunityBinding.community = itemData
            itemCommunityBinding.buttonJoin.setOnClickListener {
                onClickButtonJoin(itemCommunityBinding.buttonJoin.text == context.getString(R.string.title_out))
            }
        }
    }
}
