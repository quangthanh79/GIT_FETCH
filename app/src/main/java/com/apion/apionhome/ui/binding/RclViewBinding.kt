package com.apion.apionhome.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.apion.apionhome.base.BindAbleAdapter

@Suppress("UNCHECKED_CAST")
@BindingAdapter("data")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: List<T>?) {
    (recyclerView.adapter as? BindAbleAdapter<List<T>>)?.setData(data)
}
@Suppress("UNCHECKED_CAST")
@BindingAdapter("text")
fun <T> setText(txtView: TextView, text : String) {
    txtView.text = text
}
@Suppress("UNCHECKED_CAST")
@BindingAdapter("dataSlider")
fun <C> setRecyclerViewProperties(viewPager2: ViewPager2, data: List<C>?) {
    (viewPager2.adapter as? BindAbleAdapter<List<C>>)?.setData(data)
}