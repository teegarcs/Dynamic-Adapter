package com.teegarcs.dynamicadapter

import android.view.View
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
     * Item identifier for use in the DiffUtil to better determine if items/rows are the same.
     * This would be similar to passing in a unique identifier into the row that won't change
     * even if the other content changes.
     *
     * Defaults to layout ID and the section matcher, which may not be unique but this allows
     * for us to not have to implement this on items where its not needed.
     */
    open fun getItemId(): Int = getLayoutId() + sectionMatcher()

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


    /**
     * Optional Resource ID that can be used for section headers and sticky headers.
     * This will be ignored unless you set the DynamicSectionHeader to your RecyclerView as an Item Decorator
     */
    @LayoutRes
    open fun getHeaderLayoutId(): Int? = null

    /**
     * Override this function if you want to set a specific matcher to be used for indicating
     * if this is part of a specific section or not. If you want to do something with text,
     * simply make each section implement, grab the text and add .hashCode()
     *
     *  override fun sectionMatcher(): Int {
     *      return body.substring(0, 1).hashCode()
     *  }
     */
    open fun sectionMatcher(): Int {
        return getLayoutId()
    }

    /**
     * This function gets called right before the header is drawn on the screen.
     * Find views, set titles, images, colors, etc here.
     *
     *  override fun bindHeaderLayout(view: View) {
     *      super.bindHeaderLayout(view)
     *      view.findViewById<TextView>(R.id.list_item_section_text).text = body.substring(0, 1)
     *   }
     */
    open fun bindHeaderLayout(view: View) {}
}
