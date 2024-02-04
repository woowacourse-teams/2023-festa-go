package com.festago.festago.presentation.ui.artistdetail.adapter.artistlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState

class ArtistAdapter : ListAdapter<ArtistUiState, ArtistViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return ArtistViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ArtistUiState>() {
            override fun areItemsTheSame(oldItem: ArtistUiState, newItem: ArtistUiState): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArtistUiState, newItem: ArtistUiState): Boolean {
                return oldItem == newItem
            }
        }
    }
}
