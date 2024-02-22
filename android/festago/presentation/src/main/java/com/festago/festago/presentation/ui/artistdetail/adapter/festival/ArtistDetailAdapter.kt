package com.festago.festago.presentation.ui.artistdetail.adapter.festival

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.StageUiState

class ArtistDetailAdapter(
    private val onArtistClick: (Long) -> Unit,
) : ListAdapter<StageUiState, ArtistDetailFestivalViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistDetailFestivalViewHolder {
        return ArtistDetailFestivalViewHolder.of(parent, onArtistClick)
    }

    override fun onBindViewHolder(holder: ArtistDetailFestivalViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<StageUiState>() {
            override fun areItemsTheSame(oldItem: StageUiState, newItem: StageUiState): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StageUiState, newItem: StageUiState): Boolean {
                return oldItem == newItem
            }
        }
    }
}
