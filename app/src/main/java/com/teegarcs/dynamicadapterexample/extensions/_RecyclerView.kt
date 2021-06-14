package com.teegarcs.dynamicadapterexample.extensions

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.teegarcs.dynamicadapter.DynamicAdapter
import com.teegarcs.dynamicadapter.DynamicModel

@BindingAdapter("genericItems")
fun RecyclerView.setGenericItems(items: List<DynamicModel>?) {
    (adapter as? DynamicAdapter) ?: run {
        adapter = DynamicAdapter()
        ViewTreeLifecycleOwner.get(this)?.lifecycle?.addObserver(adapter as DynamicAdapter)
    }

    (adapter as DynamicAdapter).submitList(items)
}