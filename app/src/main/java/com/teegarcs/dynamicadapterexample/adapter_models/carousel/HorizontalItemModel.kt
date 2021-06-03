package com.teegarcs.dynamicadapterexample.adapter_models.carousel

import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.R

class HorizontalItemModel(val imageUrl: String) : DynamicModel() {
    override fun getLayoutId() = R.layout.horizontal_item_layout
}
