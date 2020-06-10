package com.teegarcs.dynamicadapterexample

import androidx.databinding.ViewDataBinding

class SampleClickableLabelModel(label: String) : SampleLabelModel(label) {

    override fun bindVariables(binding: ViewDataBinding) {
        super.bindVariables(binding)
        binding.root.setOnClickListener {
            getAdapterAction<ClickLabelAction>()?.printRow(labelLD.value)
        }
    }

}