package com.teegarcs.dynamicadapterexample.adapter_models.carousel

import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.R

class HorizontalCarouselModel(imageUrls: List<String>) : DynamicModel() {

    val items = imageUrls.map { HorizontalItemModel(it) }

    override fun getLayoutId() = R.layout.horizontal_carousel_layout
}
