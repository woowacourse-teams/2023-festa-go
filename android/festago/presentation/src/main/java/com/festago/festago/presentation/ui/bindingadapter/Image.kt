package com.festago.festago.presentation.ui.bindingadapter

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
        .placeholder(R.color.background_gray_03)
        .error(R.color.background_gray_03)
        .fallback(R.color.background_gray_03)
        .into(this)
}

@BindingAdapter("imageUrl", "blurRadius", "blurSampling")
fun ImageView.setBlurImage(imageUrl: String?, blurRadius: Int, blurSampling: Int) {
    val transformation = BlurTransformation(blurRadius, blurSampling)
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.color.background_gray_03)
        .apply(RequestOptions.bitmapTransform(transformation))
        .error(R.color.background_gray_03)
        .fallback(R.color.background_gray_03)
        .into(this)
}
