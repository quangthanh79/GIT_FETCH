package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.ItemHouseBinding

class HouseAdapter(private val listener: (House) -> Unit) : BaseAdapter<House, ItemHouseBinding>()
{

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<House, ItemHouseBinding> {
        return HouseViewHolder(
            ItemHouseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    class HouseViewHolder(
        private val itemHouseBinding: ItemHouseBinding,
        listener: (House) -> Unit
    ) : BaseViewHolder<House, ItemHouseBinding>(itemHouseBinding, listener) {
        override fun onBind(itemData: House) {
            super.onBind(itemData)
            itemHouseBinding.house = itemData
        }
    }
}
