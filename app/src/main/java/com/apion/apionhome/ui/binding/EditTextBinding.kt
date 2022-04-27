package com.apion.apionhome.ui.binding

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun bindError(view:TextInputLayout, errorText: String?) {
    println("error $errorText")
    view.error = errorText
}