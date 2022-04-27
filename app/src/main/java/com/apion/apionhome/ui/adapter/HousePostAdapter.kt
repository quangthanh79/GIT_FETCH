package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.House
import com.apion.apionhome.databinding.ItemHouseBinding
import com.apion.apionhome.databinding.ItemPostHouseBinding

class HousePostAdapter(private val listener: (House) -> Unit) : BaseAdapter<House, ItemPostHouseBinding>()
{

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<House, ItemPostHouseBinding> {

        return HousePostViewHolder(
            ItemPostHouseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        ) {
            val index = currentList.indexOf(it)
            index == currentList.size - 1
        }
    }

    class HousePostViewHolder(
        private val itemHouseBinding: ItemPostHouseBinding,
        listener: (House) -> Unit,
        val isFinish: (House) -> Boolean
    ) : BaseViewHolder<House, ItemPostHouseBinding>(itemHouseBinding, listener) {
        override fun onBind(itemData: House) {
            super.onBind(itemData)
            itemHouseBinding.house = itemData
            if (isFinish(itemData)){
                itemHouseBinding.deliver.visibility = View.GONE
            }
        }
    }
}
