package com.apion.apionhome.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.apion.apionhome.R
import com.apion.apionhome.data.model.ImagePicker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.imageview.ShapeableImageView
import java.io.File

class ImagePickerAdapter(
    context: Context,
    list: MutableList<ImagePicker>,
    val onPickerClick: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private val context: Context = context
    var list: List<ImagePicker> = list

    fun addImage(path: String) {
        val last = list.last()
        val tmpTmpList = list.subList(0, list.size - 1)
        val listResult = mutableListOf<ImagePicker>()
        listResult.addAll(tmpTmpList)
        listResult.add(ImagePicker(VIEW_TYPE_ONE, path))
        listResult.add(last)
        list = listResult
        notifyDataSetChanged()
    }

    private inner class ImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imgView: ShapeableImageView = itemView.findViewById(R.id.imgImagePicker)
        var btnRemoveImage: ImageButton = itemView.findViewById(R.id.btnRemove)

        fun bind(position: Int) {
            val itemData = list[position]
            btnRemoveImage.setOnClickListener {
                val first = list.subList(0, position)
                val last = list.subList(position + 1, list.size)
                val listResult = mutableListOf<ImagePicker>()
                listResult.addAll(first)
                listResult.addAll(last)
                list = listResult
                notifyDataSetChanged()
            }
            itemData.data?.let {
                if (it.contains("http")) {
                    Glide.with(context)
                        .load(it)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.img_no_image_to_show)
                        .into(imgView)
                } else {
                    Glide.with(context)
                        .load(File(it))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.img_no_image_to_show)
                        .into(imgView)
                }
            }
        }
    }

    private inner class PickerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var btnPickerImage: ImageButton = itemView.findViewById(R.id.btnImagePicker)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]

            btnPickerImage.setOnClickListener {
                onPickerClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return ImageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
            )
        }
        return PickerViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_pick_image, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType == VIEW_TYPE_ONE) {
            (holder as ImageViewHolder).bind(position)
        } else {
            (holder as PickerViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}
