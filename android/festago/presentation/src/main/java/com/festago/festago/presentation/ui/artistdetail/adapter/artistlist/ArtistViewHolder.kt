package com.festago.festago.presentation.ui.artistdetail.adapter.artistlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemArtistDetailArtistBinding
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState

class ArtistViewHolder(
    private val binding: ItemArtistDetailArtistBinding,
    onArtistClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            binding.artist?.id?.let(onArtistClick)
        }
    }

    fun bind(item: ArtistUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup, onArtistClick: (Long) -> Unit): ArtistViewHolder {
            val binding = ItemArtistDetailArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding, onArtistClick)
        }
    }
}
