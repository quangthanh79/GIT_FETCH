package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.local.ILocation
import com.apion.apionhome.databinding.ItemLocationBinding

@Suppress("UNCHECKED_CAST")
class SearchLocationAdapter<L : ILocation>(
    private val listener: (L) -> Unit
) : BaseAdapter<ILocation, ItemLocationBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ILocation, ItemLocationBinding> {
        val itemView = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchLocationViewHolder(itemView, listener)
    }

    private class SearchLocationViewHolder<L : ILocation>(private val itemLocationBinding: ItemLocationBinding, listener: (L) -> Unit,)
        : BaseViewHolder<ILocation, ItemLocationBinding>(itemLocationBinding, listener as? (ILocation) -> Unit ?: {})
    {
        override fun onBind(itemData: ILocation) {
            super.onBind(itemData)
            itemLocationBinding.location = itemData

        }

    }
}
