package com.apion.apionhome.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.apion.apionhome.base.BindAbleAdapter
import com.facebook.shimmer.ShimmerFrameLayout

@Suppress("UNCHECKED_CAST")
@BindingAdapter("dataShimmerList")
fun <T> setupShimmerList(shimmer: ShimmerFrameLayout, data: List<T>?) {
    if (data.isNullOrEmpty() && !shimmer.isAnimationStarted) shimmer.startShimmerAnimation()
    else shimmer.stopShimmerAnimation()
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("dataShimmer")
fun <T> setupShimmer(shimmer: ShimmerFrameLayout, data: T?) {
    if (data == null) shimmer.startShimmerAnimation()
    else if (shimmer.isAnimationStarted) shimmer.stopShimmerAnimation()
}