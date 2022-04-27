package com.apion.apionhome.base

import androidx.recyclerview.widget.DiffUtil
import com.apion.apionhome.data.model.GeneraEntity

class BaseDiffUtilString : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
