package com.festago.festago.presentation.util

import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat

fun View.setOnApplyWindowInsetsCompatListener(listener: OnApplyWindowInsetsListener) {
    ViewCompat.setOnApplyWindowInsetsListener(this, listener)
}
