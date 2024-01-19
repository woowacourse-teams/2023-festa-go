package com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground

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
            item.artists.joinToString(", ") { it.name }
    }

    companion object {
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
