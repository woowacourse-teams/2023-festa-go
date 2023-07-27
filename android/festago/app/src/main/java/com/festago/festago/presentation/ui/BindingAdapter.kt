package com.festago.festago.presentation.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.festago.festago.R

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun ImageView.setImage(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .error(R.drawable.ic_image_placeholder)
        .fallback(R.drawable.ic_image_placeholder)
        .into(this)
}
