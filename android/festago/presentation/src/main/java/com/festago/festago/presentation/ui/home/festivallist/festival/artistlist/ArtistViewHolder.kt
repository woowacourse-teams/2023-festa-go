package com.festago.festago.presentation.ui.home.festivallist.festival.artistlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemFestivalListArtistBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.ArtistUiState

class ArtistViewHolder(
    private val binding: ItemFestivalListArtistBinding,
    onArtistClick: (ArtistUiState) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            onArtistClick(binding.artist!!)
        }
    }

    fun bind(item: ArtistUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup, onArtistClick: (ArtistUiState) -> Unit): ArtistViewHolder {
            val binding = ItemFestivalListArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding, onArtistClick)
        }
    }
}
