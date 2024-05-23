package com.festago.festago.presentation.ui.search.festivalsearch.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemSearchFestivalArtistBinding
import com.festago.festago.presentation.ui.search.uistate.ArtistUiState

class ArtistViewHolder(
    private val binding: ItemSearchFestivalArtistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ArtistUiState) {
        binding.item = item
    }

    companion object {
        fun of(parent: ViewGroup): ArtistViewHolder {
            val binding = ItemSearchFestivalArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding)
        }
    }
}
