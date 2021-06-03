package com.teegarcs.dynamicadapterexample.adapter_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class TimerLabelModel : DynamicModel() {

    val labelLD = MutableLiveData<String>().apply { value = "Shown for 0 seconds" }
    private var counter: Int = 0

    override fun getLayoutId() = R.layout.timer_label_layout

    init {
        //keeps time of how long the user has been looking at the view.
        // Only runs when the view is seen by the user
        lifecycleScope.launchWhenResumed {
            while (isActive) {
                delay(1000)
                counter += 1
                labelLD.value = "Shown for $counter seconds"
            }
        }
    }
}
