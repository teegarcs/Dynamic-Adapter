package com.teegarcs.dynamicadapterexample

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapter.DynamicModelActionCallback
import com.teegarcs.dynamicadapterexample.databinding.SampleClickableLabelViewBinding

class SampleClickableLabelModel(label: String, private val action: ClickLabelAction) :
    DynamicModel() {

    val labelLD = MutableLiveData<String>().apply { value = label }

    override fun getLayoutId() = R.layout.sample_clickable_label_view

    override fun bindVariables(binding: ViewDataBinding) {
        super.bindVariables(binding)
        (binding as? SampleClickableLabelViewBinding)?.run {
            sampleLabelText.setOnLongClickListener {
                action.printRow("Long Click" + labelLD.value)
                true
            }
        }
    }

    /**
     * Provide an action to the DataBinding.. Optional and if not being used by binding
     * can just be ignored.
     */
    override fun getModelActionCallback(): DynamicModelActionCallback? {
        return action
    }

}