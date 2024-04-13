package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemFestivalBookmarkArtistBinding

class ArtistViewHolder(
    private val binding: ItemFestivalBookmarkArtistBinding,
    onArtistClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            onArtistClick(binding.artist!!.id)
        }
    }

    fun bind(item: ArtistUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup, onArtistClick: (Long) -> Unit): ArtistViewHolder {
            val binding = ItemFestivalBookmarkArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding, onArtistClick)
        }
    }
}
