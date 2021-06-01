package com.teegarcs.dynamicadapterexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapter.DynamicModelActionCallback

class CheckBoxModel(private val actions: CheckBoxAction) : DynamicModel() {
    private val _isChecked = MutableLiveData<Boolean>().apply { value = false }
    val isChecked: LiveData<Boolean> = _isChecked

    override fun getLayoutId() = R.layout.check_box_layout

    override fun getModelActionCallback() = actions

    fun setIsChecked(checked: Boolean) {
        _isChecked.value = checked
    }
}

interface CheckBoxAction : DynamicModelActionCallback {

    fun itemChecked(model: CheckBoxModel)

}