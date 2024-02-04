package com.festago.festago.presentation.ui.artistdetail.adapter.artistlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemArtistDetailArtistBinding
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState

class ArtistViewHolder(
    private val binding: ItemArtistDetailArtistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ArtistUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup): ArtistViewHolder {
            val binding = ItemArtistDetailArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding)
        }
    }
}
