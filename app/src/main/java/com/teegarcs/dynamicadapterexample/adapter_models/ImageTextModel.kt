package com.teegarcs.dynamicadapterexample.adapter_models

import android.view.View
import android.widget.TextView
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.R

class ImageTextModel(val imageUrl: String, val body: String) : DynamicModel() {
    val defaultImage: Int = R.mipmap.ic_launcher
    override fun getLayoutId() = R.layout.image_text_layout

    override fun getHeaderLayoutId() = R.layout.sample_section_header_two

    override fun sectionMatcher(): Int {
        return body.substring(0, 1).hashCode()
    }

    override fun bindHeaderLayout(view: View) {
        super.bindHeaderLayout(view)
        view.findViewById<TextView>(R.id.list_item_section_text).text = body.substring(0, 1)
    }
}
