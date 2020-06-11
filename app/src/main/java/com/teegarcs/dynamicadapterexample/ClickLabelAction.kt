package com.teegarcs.dynamicadapterexample

import com.teegarcs.dynamicadapter.DynamicModelActionCallback

interface ClickLabelAction : DynamicModelActionCallback {

    fun printRow(label: String?)
}