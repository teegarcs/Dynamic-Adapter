package com.teegarcs.dynamicadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic ViewHolder used to hold the inflated view of the [DynamicAdapter]
 *
 * @property itemView - the view inflated for the RecyclerView
 */
class DynamicModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
