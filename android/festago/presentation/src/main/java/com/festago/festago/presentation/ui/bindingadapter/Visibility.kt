package com.festago.festago.presentation.ui.bindingadapter

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.doOnLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibilityOnMotionLayout")
fun View.setVisibilityOnMotionLayout(visible: Boolean) {
    val layout = this.parent as MotionLayout
    doOnLayout {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }
    layout.constraintSetIds.forEach {
        layout.getConstraintSet(it).setVisibility(this.id, if (visible) View.VISIBLE else View.GONE)
    }
}
