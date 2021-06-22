package com.teegarcs.dynamicadapterexample.adapter_models

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapter.DynamicModelActionCallback
import com.teegarcs.dynamicadapterexample.R

class CheckBoxModel(private val actions: CheckBoxAction) : DynamicModel() {
    private val _isChecked = MutableLiveData<Boolean>().apply { value = false }
    val isChecked: LiveData<Boolean> = _isChecked

    override fun getLayoutId() = R.layout.check_box_layout

    override fun getModelActionCallback() = actions

    fun setIsChecked(checked: Boolean) {
        Log.i("****", lifecycle.currentState.toString())
        _isChecked.value = checked
    }

    override fun getHeaderLayoutId() = R.layout.sample_section_header_one

    override fun sectionMatcher(): Int {
        return R.layout.check_box_layout
    }

    override fun bindHeaderLayout(view: View) {
        super.bindHeaderLayout(view)
        view.findViewById<TextView>(R.id.list_item_section_text).text = "Please Select One"
    }
}

interface CheckBoxAction : DynamicModelActionCallback {
    fun itemChecked(model: CheckBoxModel)
}
