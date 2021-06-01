package com.teegarcs.dynamicadapterexample.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(context).load(url).into(this)
    }
}
