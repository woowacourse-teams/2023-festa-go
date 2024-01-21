package com.festago.festago.presentation.ui

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.doOnLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.festago.festago.presentation.R

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

@BindingAdapter("imageUrl")
fun ImageView.setImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .error(R.drawable.ic_image_placeholder)
        .fallback(R.drawable.ic_image_placeholder)
        .into(this)
}

@BindingAdapter("background")
fun View.setBackground(resourceId: Int) {
    setBackgroundResource(resourceId)
}
