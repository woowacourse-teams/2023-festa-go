package com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemPopularFestivalForegroundBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

class PopularFestivalForegroundViewHolder(
    private val binding: ItemPopularFestivalForegroundBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FestivalItemUiState) {
        binding.item = item
        binding.ivPopularFestivalImage.outlineProvider
        binding.tvPopularFestivalArtistsName.text =
            item.artists.joinToString(ARTIST_NAME_JOIN_SEPARATOR) { it.name }
        setImageSizeFlexibleToScreenWidth()
    }

    private fun setImageSizeFlexibleToScreenWidth() {
        val imageSize = Resources.getSystem().configuration.screenWidthDp - MINUS_TO_IMAGE_SIZE
        val density = Resources.getSystem().displayMetrics.density

        binding.ivPopularFestivalImage.layoutParams.apply {
            this.width = (imageSize * density + ROUNDUP_CONDITION).toInt()
            this.height = (imageSize * density + ROUNDUP_CONDITION).toInt()
        }
    }

    companion object {
        private const val MINUS_TO_IMAGE_SIZE = 160
        private const val ROUNDUP_CONDITION = 0.5f
        private const val ARTIST_NAME_JOIN_SEPARATOR = ", "

        fun of(parent: ViewGroup): PopularFestivalForegroundViewHolder {
            val binding = ItemPopularFestivalForegroundBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return PopularFestivalForegroundViewHolder(binding)
        }
    }
}
