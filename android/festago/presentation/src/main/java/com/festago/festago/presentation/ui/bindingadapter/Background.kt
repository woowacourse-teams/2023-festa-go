package com.festago.festago.presentation.ui.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("background")
fun View.setBackground(resourceId: Int) {
    setBackgroundResource(resourceId)
}
