package com.festago.festago.presentation.ui.schooldetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemSchoolDetailArtistBinding
import com.festago.festago.presentation.ui.schooldetail.uistate.ArtistUiState

class ArtistViewHolder(
    private val binding: ItemSchoolDetailArtistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ArtistUiState) {
        binding.artist = item
    }

    companion object {
        fun of(parent: ViewGroup): ArtistViewHolder {
            val binding = ItemSchoolDetailArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistViewHolder(binding)
        }
    }
}
