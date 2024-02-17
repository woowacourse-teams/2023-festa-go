package com.festago.festago.presentation.ui.home.schooldetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ViewSocialMediaBinding

class SocialMediaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    logoUrl: String,
    linkUrl: String,
) : ConstraintLayout(context, attrs) {
    private val binding: ViewSocialMediaBinding =
        ViewSocialMediaBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        Glide.with(context)
            .load(logoUrl)
            .error(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_launcher_background)
            .into(binding.ivSocialMedia)

        binding.clSocialMedia.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
            context.startActivity(intent)
        }
    }
}
