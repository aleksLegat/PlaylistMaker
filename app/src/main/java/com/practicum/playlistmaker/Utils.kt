package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.View

fun dpToPx(dp: Float, view: View) : Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        view.context.resources.displayMetrics).toInt()
}