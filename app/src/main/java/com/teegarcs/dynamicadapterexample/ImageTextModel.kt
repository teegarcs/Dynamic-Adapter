package com.teegarcs.dynamicadapterexample

import com.teegarcs.dynamicadapter.DynamicModel

class ImageTextModel(val imageUrl: String, val body: String) : DynamicModel() {
    override fun getLayoutId() = R.layout.image_text_layout
}
