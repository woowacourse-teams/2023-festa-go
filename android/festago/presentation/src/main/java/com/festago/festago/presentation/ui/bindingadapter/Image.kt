package com.festago.festago.presentation.ui.bindingadapter

import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.festago.festago.presentation.R
import jp.wasabeef.glide.transformations.BlurTransformation

@BindingAdapter("imageUrl")
fun ImageView.setImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.bg_festago_default)
        .error(R.drawable.bg_festago_default)
        .fallback(R.drawable.bg_festago_default)
        .into(this)
}

@BindingAdapter("imageUrl", "blurRadius", "blurSampling")
fun ImageView.setBlurImage(imageUrl: String?, blurRadius: Int, blurSampling: Int) {
    val transformation = BlurTransformation(blurRadius, blurSampling)
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.bg_festago_default)
        .apply(RequestOptions.bitmapTransform(transformation))
        .error(R.drawable.bg_festago_default)
        .fallback(R.drawable.bg_festago_default)
        .into(this)
}

@BindingAdapter("elevatedImageUrl")
fun ImageView.setElevatedImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.bg_festago_default)
        .error(R.drawable.bg_festago_default)
        .fallback(R.drawable.bg_festago_default)
        .into(this)
    this.elevation = 12f
    this.outlineProvider = ViewOutlineProvider.BOUNDS
}
