package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.apion.apionhome.base.BaseDiffUtilString
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.base.BindAbleAdapter
import com.apion.apionhome.data.model.local.ILocation
import com.apion.apionhome.databinding.ItemDetailBannerImageBinding
import com.apion.apionhome.databinding.ItemDetailImageBinding

class ImageDetailBannerAdapter(private val listener: (String) -> Unit) :
    ListAdapter<String, BaseViewHolder<String, ItemDetailBannerImageBinding>>(BaseDiffUtilString()),
    BindAbleAdapter<List<String>> {

    fun getCurrentIndex(path: String): Int {
        val index = currentList.indexOf(path)
        return if (index == -1) index else 0
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<String, ItemDetailBannerImageBinding>,
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
    ): BaseViewHolder<String, ItemDetailBannerImageBinding> {
        val itemView = ItemDetailBannerImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(itemView, listener)
    }

    private class ImageViewHolder(
        private val itemDetailImageBinding: ItemDetailBannerImageBinding,
        listener: (String) -> Unit,
    ) : BaseViewHolder<String, ItemDetailBannerImageBinding>(
        itemDetailImageBinding, listener
    ) {
        override fun onBind(itemData: String) {
            super.onBind(itemData)
            itemDetailImageBinding.image = itemData
        }
    }
}
