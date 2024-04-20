package com.festago.festago.presentation.ui.artistdetail.adapter.festival

import android.view.LayoutInflater
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemArtistDetailMoreItemBinding
import com.festago.festago.presentation.ui.artistdetail.uistate.MoreItemUiState

class ArtistDetailMoreItemViewHolder(
    binding: ItemArtistDetailMoreItemBinding
) : ArtistDetailViewHolder(binding) {
    fun bind(item: MoreItemUiState) {
        item.requestMore()
    }

    companion object {
        fun of(parent: ViewGroup): ArtistDetailMoreItemViewHolder {
            val binding = ItemArtistDetailMoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistDetailMoreItemViewHolder(binding)
        }
    }
}
