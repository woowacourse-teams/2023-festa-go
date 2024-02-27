package com.festago.festago.presentation.ui.festivaldetail.adapter.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemFestivalDetailStageArtistBinding
import com.festago.festago.presentation.ui.festivaldetail.uiState.ArtistItemUiState

class ArtistViewHolder(
    private val binding: ItemFestivalDetailStageArtistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ArtistItemUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup): ArtistViewHolder {
            val binding = ItemFestivalDetailStageArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding)
        }
    }
}
