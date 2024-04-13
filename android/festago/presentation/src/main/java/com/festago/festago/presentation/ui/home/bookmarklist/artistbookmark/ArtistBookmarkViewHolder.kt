package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemArtistBookmarkBinding

class ArtistBookmarkViewHolder(val binding: ItemArtistBookmarkBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ArtistBookmarkUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup): ArtistBookmarkViewHolder {
            val binding = ItemArtistBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistBookmarkViewHolder(binding)
        }
    }
}
