package com.teegarcs.dynamicadapterexample.adapter_models.carousel

import androidx.databinding.ViewDataBinding
import com.teegarcs.dynamicadapter.DynamicAdapter
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.R
import com.teegarcs.dynamicadapterexample.databinding.HorizontalCarouselLayoutBinding

class HorizontalCarouselModel(imageUrls: List<String>) : DynamicModel() {

    private val adapter = DynamicAdapter()

    init {
        lifecycle.addObserver(adapter)
        adapter.submitList(
            imageUrls.map { HorizontalItemModel(it) }
        )
    }

    override fun getLayoutId() = R.layout.horizontal_carousel_layout

    override fun bindVariables(binding: ViewDataBinding) {
        super.bindVariables(binding)
        (binding as? HorizontalCarouselLayoutBinding)?.let {
            binding.horizontalRecycler.adapter = adapter
        }
    }
}
