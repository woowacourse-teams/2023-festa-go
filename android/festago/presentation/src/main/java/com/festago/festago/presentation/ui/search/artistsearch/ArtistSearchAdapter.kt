package com.festago.festago.presentation.ui.search.artistsearch

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.search.uistate.ArtistSearchItemUiState

class ArtistSearchAdapter : ListAdapter<ArtistSearchItemUiState, ArtistSearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistSearchViewHolder {
        return ArtistSearchViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: ArtistSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<ArtistSearchItemUiState>() {
            override fun areItemsTheSame(
                oldItem: ArtistSearchItemUiState,
                newItem: ArtistSearchItemUiState,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ArtistSearchItemUiState,
                newItem: ArtistSearchItemUiState,
            ): Boolean = oldItem == newItem
        }
    }
}
