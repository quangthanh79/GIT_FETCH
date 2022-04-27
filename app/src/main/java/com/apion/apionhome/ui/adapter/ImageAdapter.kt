package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.apion.apionhome.base.BaseDiffUtilString
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.base.BindAbleAdapter
import com.apion.apionhome.data.model.local.ILocation
import com.apion.apionhome.databinding.ItemDetailImageBinding

@Suppress("UNCHECKED_CAST")
class ImageAdapter(private val listener: (String) -> Unit) :
    ListAdapter<String, BaseViewHolder<String, ItemDetailImageBinding>>(BaseDiffUtilString()),
    BindAbleAdapter<List<String>> {

    override fun onBindViewHolder(
        holder: BaseViewHolder<String, ItemDetailImageBinding>,
        position: Int
    ) {
        holder.onBind(getItem(position))
    }

    override fun setData(data: List<String>?) {
        submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<String, ItemDetailImageBinding> {
        val itemView = ItemDetailImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(itemView, listener)
    }

    private class ImageViewHolder<L : ILocation>(
        private val itemDetailImageBinding: ItemDetailImageBinding,
        listener: (L) -> Unit,
    ) : BaseViewHolder<String, ItemDetailImageBinding>(
        itemDetailImageBinding, listener as? (String) -> Unit ?: {}
    ) {
        override fun onBind(itemData: String) {
            super.onBind(itemData)
            itemDetailImageBinding.image = itemData
        }
    }
}
