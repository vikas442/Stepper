package com.vikas442.stepper

import android.content.Context
import android.util.TypedValue

fun Int.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), context.resources.displayMetrics)
}
fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this, context.resources.displayMetrics)
}