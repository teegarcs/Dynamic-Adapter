package com.teegarcs.dynamicadapterexample

import androidx.lifecycle.MutableLiveData
import com.teegarcs.dynamicadapter.DynamicModel

class SampleLabelModel(label: String) : DynamicModel() {

    val labelLD = MutableLiveData<String>().apply { value = label }

    override fun getLayoutId() = R.layout.sample_label_view

}
