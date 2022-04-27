package com.apion.apionhome.base

import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.apion.apionhome.data.model.GeneraEntity
//BaseAdapter<Community, ItemCommunityBinding>
abstract class BaseAdapter<T: GeneraEntity, B : ViewBinding>() :
    ListAdapter<T, BaseViewHolder<T, B>>(BaseDiffUtil<T>()),
    BindAbleAdapter<List<T>>
{
  // thay đổi BaseViewHolder<T, B> thành BaseViewHolder<GeneraEntity, B>
    override fun onBindViewHolder(holder: BaseViewHolder<T, B>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun setData(data: List<T>?) {
        submitList(data)
    }
}
