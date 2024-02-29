package com.festago.festago.presentation.util

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import com.festago.festago.presentation.R

@Suppress("DEPRECATION")
fun Activity.setStatusBarMode(
    isLight: Boolean,
    backgroundColor: Int?,
) {
    window.statusBarColor = when {
        backgroundColor != null -> backgroundColor
        isLight -> ContextCompat.getColor(this, R.color.background_gray_01)
        else -> Color.BLACK
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        window.insetsController?.setSystemBarsAppearance(
            if (isLight) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
        )
    } else {
        val lFlags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility =
            if (isLight.not()) {
                lFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                lFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
    }
}
