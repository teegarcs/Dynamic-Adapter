package com.teegarcs.dynamicadapterexample.adapter_models

import androidx.lifecycle.MutableLiveData
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapter.DynamicModelActionCallback
import com.teegarcs.dynamicadapterexample.R

class ImageTextWithButtonsModel(
    val imageUrl: String,
    val body: String,
    private val actions: ImageTextButtonActions
) : DynamicModel() {

    val backgroundColor = MutableLiveData<String>().apply { value = "#ffffff" }
    override fun getLayoutId() = R.layout.image_text_with_buttons_layout
    override fun getModelActionCallback() = actions

    fun swapColor() {
        if (backgroundColor.value == "#ffffff") {
            backgroundColor.value = "#42d1f5"
        } else {
            backgroundColor.value = "#ffffff"
        }
    }
}

interface ImageTextButtonActions : DynamicModelActionCallback {

    fun showToast(body: String)

    fun removeItem(model: ImageTextWithButtonsModel)
}
