package com.teegarcs.dynamicadapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * Model to be implemented by any class that wishes to show data on the DynamicAdapter.
 *
 * This provides a lifecycle owner so that LiveData and DataBinding can be used. The Lifecycle
 * is managed by the adapter based on the status of the view being attached to the recyclerview or
 * not
 *
 */
abstract class DynamicModel() : LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    /**
     * Called during the onBind of a ViewHolder. The Super will take care of binding the
     * LifecycleOwner and variables defined with the generic naming patterns. Override this function
     * if you need to perform custom actions within the bind, or if you have variable names
     * that are not listed below:
     *
     * <variable
     * name="model"
     * type="com.teegarcs.dynamicadapter.DynamicModel" />
     *
     * <variable
     * name="modelAction"
     * type="com.teegarcs.dynamicadapter.DynamicModelActionCallback" />
     *
     * @param binding - the ViewBinding being bound to this model.
     */
    open fun bindVariables(binding: ViewDataBinding) {
        binding.lifecycleOwner = this
        binding.setVariable(BR.model, this)
        binding.setVariable(BR.adapterAction, getModelActionCallback())
    }


    /**
     * Layout Resource ID for the layout this model will inflate.
     *
     * @return Layout Resource ID
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Used internally to return the lifecycle registry to the adapter so that
     * the model's lifecycle can be updated based on whether it is currently showing or not.
     *
     * @return LifecycleRegistry
     */
    internal fun getLifecycleRegistry(): LifecycleRegistry = lifecycleRegistry

    /**
     * Get the Lifecycle for the Model.
     *
     * @return Lifecycle
     */
    final override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    /**
     * DynamicModelActionCallback - optional callback for actions that need to be passed up
     * to a fragment or activity
     */
    open fun getModelActionCallback(): DynamicModelActionCallback? {
        return null
    }
}
