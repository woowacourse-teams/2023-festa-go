package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemArtistBookmarkBinding
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.uistate.FestivalBookmarkItemUiState

class ArtistBookmarkAdapter : ListAdapter<ArtistBookmarkUiState, ArtistBookmarkViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistBookmarkViewHolder {
        return ArtistBookmarkViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: ArtistBookmarkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ArtistBookmarkUiState>() {
            override fun areItemsTheSame(
                oldItem: ArtistBookmarkUiState,
                newItem: ArtistBookmarkUiState,
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: ArtistBookmarkUiState,
                newItem: ArtistBookmarkUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
