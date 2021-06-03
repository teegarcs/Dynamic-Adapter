package com.teegarcs.dynamicadapterexample.extensions

import android.graphics.Color
import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("colorCode")
fun View.setColorCodeBackground(color: String?) {
    color?.let {
        try {
            val parsedColor = Color.parseColor(it)
            setBackgroundColor(parsedColor)
        } catch (e: Exception) {
        }
    }
}
