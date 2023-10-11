package com.vikas442.stepper

import android.graphics.RectF
import android.graphics.Shader

internal class StepItem {
    lateinit var rect: RectF
    lateinit var gd: Shader
    var checked: Boolean = false
}