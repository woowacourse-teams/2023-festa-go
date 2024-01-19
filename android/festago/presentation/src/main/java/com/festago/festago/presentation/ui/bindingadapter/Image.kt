package com.festago.festago.presentation.ui.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.festago.festago.presentation.R
import jp.wasabeef.glide.transformations.BlurTransformation

@BindingAdapter("imageUrl")
fun ImageView.setImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .error(R.drawable.ic_launcher_background)
        .fallback(R.drawable.ic_launcher_background)
        .into(this)
}

@BindingAdapter("imageUrl", "blurRadius", "blurSampling")
fun ImageView.setBlurImage(imageUrl: String?, blurRadius: Int, blurSampling: Int) {
    val transformation = BlurTransformation(blurRadius, blurSampling)
    Glide.with(context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions.bitmapTransform(transformation))
        .error(R.drawable.ic_launcher_background)
        .fallback(R.drawable.ic_launcher_background)
        .into(this)
}
