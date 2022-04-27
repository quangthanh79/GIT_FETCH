package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.ItemUserHouseBinding

class UserHouseAdapter(private val listener: (House) -> Unit) : BaseAdapter<House, ItemUserHouseBinding>()
{

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<House, ItemUserHouseBinding> {

        return UserHouseViewHolder(
            ItemUserHouseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    class UserHouseViewHolder(
        private val itemHouseBinding: ItemUserHouseBinding,
        listener: (House) -> Unit,
    ) : BaseViewHolder<House, ItemUserHouseBinding>(itemHouseBinding, listener) {
        override fun onBind(itemData: House) {
            super.onBind(itemData)
            itemHouseBinding.house = itemData
        }
    }
}
