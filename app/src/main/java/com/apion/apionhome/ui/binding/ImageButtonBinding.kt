package com.apion.apionhome.ui.binding

import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.apion.apionhome.MyApplication
import com.apion.apionhome.R

@BindingAdapter("checkBookmark")
fun checkBookmark(view: ImageButton, houseId: Int) {
    view.findViewTreeLifecycleOwner()?.let {
        MyApplication.sessionUser.observe(it) {
            var isBookMark = it?.isBookmark(houseId) ?: false
            println("isBookmark $isBookMark")
            when {
                isBookMark -> {
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_was_bookmark
                        )
                    )
                }
                else -> {
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_not_bookmark
                        )
                    )
                }
            }
        }
    }
}
