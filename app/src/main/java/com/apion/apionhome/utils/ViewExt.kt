@file:Suppress("DEPRECATION")

package com.apion.apionhome.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Insets
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout

fun View.setWith(percentWidth: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = (context as Activity).windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        layoutParams.width =
            ((windowMetrics.bounds.width() - insets.left - insets.right) * percentWidth).toInt()
        layoutParams.height = layoutParams.width
    } else {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        layoutParams.width = (displayMetrics.widthPixels * percentWidth).toInt()
        layoutParams.height = layoutParams.width
    }
}

fun View.setHeight(percent: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = (context as Activity).windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        layoutParams.height = windowMetrics.bounds.height() - insets.top - insets.top
    } else {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        layoutParams.height = (displayMetrics.heightPixels * percent).toInt()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.showAndHide() {
    this.visibility = View.VISIBLE
    Handler().postDelayed(
        {
            this.hide()
        },
        1000
    )
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.showSoftKeyboard(activity: Activity) {
    println("hide")
    val imm =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    println(imm)
    val bl = imm.hideSoftInputFromWindow(
        this.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
    println(bl)
}

fun View.convertToBitmap(): Bitmap {
    val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(measureSpec, measureSpec)
    layout(0, 0, measuredWidth, measuredHeight)
    val r = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    r.eraseColor(Color.TRANSPARENT)
    val canvas = Canvas(r)
    draw(canvas)
    return r
}

fun ConstraintLayout.extractBitmap(): Bitmap {
    this.layoutParams = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.MATCH_PARENT
    )
    val dm = context.resources.displayMetrics
    this.measure(
        View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY)
    )
    this.layout(0, 0, this.measuredWidth, this.measuredHeight)
    val bitmap = Bitmap.createBitmap(
        this.measuredWidth,
        this.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.layout(this.left, this.top, this.right, this.bottom)
    this.draw(canvas)
    return bitmap
}
