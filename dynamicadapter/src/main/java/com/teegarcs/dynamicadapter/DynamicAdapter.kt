package com.teegarcs.dynamicadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Dynamic Adapter that can be used as a one stop shop for all RecyclerViews leveraging DataBinding.
 * This adapter also manages the lifecycle provided within the [DynamicModel]
 *
 * This adapter implements [LifecycleEventObserver] so that it can be optionally attached as an
 * observer to the Activity/Fragment Lifecycle. This is helpful for maintaining the status of the
 * recyclerview.
 *
 * @property items - list of classes extending [DynamicModel] that will be shown on the recyclerview
 */
class DynamicAdapter(
) :
    ListAdapter<DynamicModel, DynamicModelViewHolder>(DynamicModelDiffCallback),
    LifecycleEventObserver {

    //maintains a list of views that are in "resumed" state
    private val resumedSet = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DynamicModelViewHolder(
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        ).root
    )


    override fun getItemViewType(position: Int): Int {
        return getItem(position).getLayoutId()
    }

    override fun onBindViewHolder(
        holder: DynamicModelViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty())
            return

        DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)?.let {
            getItem(position).bindVariables(it)
        }
    }

    override fun onBindViewHolder(holder: DynamicModelViewHolder, position: Int) {
        getItem(position).getLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START)
    }


    override fun onViewAttachedToWindow(holder: DynamicModelViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.bindingAdapterPosition

        if (position != RecyclerView.NO_POSITION) {
            getItem(holder.bindingAdapterPosition).getLifecycleRegistry()
                .handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

            resumedSet.add(position)
        }
    }

    override fun onViewDetachedFromWindow(holder: DynamicModelViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val position = holder.bindingAdapterPosition

        if (position != RecyclerView.NO_POSITION) {
            getItem(holder.bindingAdapterPosition).getLifecycleRegistry()
                .handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)

            resumedSet.remove(position)
        }
    }

    override fun onViewRecycled(holder: DynamicModelViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.bindingAdapterPosition

        if (position != RecyclerView.NO_POSITION) {
            getItem(holder.bindingAdapterPosition).getLifecycleRegistry()
                .handleLifecycleEvent(Lifecycle.Event.ON_STOP)

            resumedSet.remove(position)
        }
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE,
            Lifecycle.Event.ON_RESUME -> {
                val iterator = resumedSet.iterator()
                while (iterator.hasNext()) {
                    val position = iterator.next()
                    currentList.getOrNull(position)?.getLifecycleRegistry()
                        ?.handleLifecycleEvent(event)
                        ?: iterator.remove()
                }
            }
        }
    }

    object DynamicModelDiffCallback : DiffUtil.ItemCallback<DynamicModel>() {
        override fun areItemsTheSame(oldItem: DynamicModel, newItem: DynamicModel): Boolean {
            return oldItem.getLayoutId() == newItem.getLayoutId()
        }

        override fun areContentsTheSame(oldItem: DynamicModel, newItem: DynamicModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}
