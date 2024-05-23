package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.artistadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemFestivalBookmarkArtistBinding

class ArtistViewHolder(private val binding: ItemFestivalBookmarkArtistBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ArtistUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup): ArtistViewHolder {
            val binding = ItemFestivalBookmarkArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding)
        }
    }
}
