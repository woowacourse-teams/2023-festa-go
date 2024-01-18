package com.festago.festago.presentation.util

import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter

class OnSingleClickListener(
    private var interval: Int = 600,
    private var onSingleClick: (View) -> Unit,
) : View.OnClickListener {

    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if ((elapsedRealtime - lastClickTime) < interval) {
            return
        }
        lastClickTime = elapsedRealtime
        onSingleClick(v)
    }
}

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(onSingleClick: (View) -> Unit) {
    val oneClick = OnSingleClickListener {
        onSingleClick(it)
    }
    setOnClickListener(oneClick)
}
