package com.teegarcs.dynamicadapterexample.adapter_models

import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.R

class ImageTextModel(val imageUrl: String, val body: String) : DynamicModel() {
    override fun getLayoutId() = R.layout.image_text_layout
}
