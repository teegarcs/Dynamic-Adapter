package com.teegarcs.dynamicadapterexample.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["imageUrl", "placeHolderDrawable", "errorDrawable"], requireAll = false)
fun ImageView.loadImage(
    url: String?,
    @DrawableRes placeHolderDrawable: Int? = null ,
    @DrawableRes errorDrawable: Int? = null
) {

    Glide.with(context).load(url).apply {
        placeHolderDrawable?.let {
            placeholder(it)
        }
        errorDrawable?.let {
            error(it)
        }
    }.into(this)
}
