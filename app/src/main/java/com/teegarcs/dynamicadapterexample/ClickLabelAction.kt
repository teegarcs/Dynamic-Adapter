package com.teegarcs.dynamicadapterexample

import com.teegarcs.dynamicadapter.DynamicAdapterActionCallback

interface ClickLabelAction : DynamicAdapterActionCallback {

    fun printRow(label: String?)
}