package com.festago.festago.presentation.ui.festivaldetail.adapter.artist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.festivaldetail.uiState.ArtistItemUiState

class ArtistAdapter : ListAdapter<ArtistItemUiState, ArtistViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return ArtistViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ArtistItemUiState>() {
            override fun areItemsTheSame(
                oldItem: ArtistItemUiState,
                newItem: ArtistItemUiState,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ArtistItemUiState,
                newItem: ArtistItemUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
