package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.adapater

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.uistate.FestivalBookmarkItemUiState

class FestivalBookmarkViewAdapter :
    ListAdapter<FestivalBookmarkItemUiState, FestivalBookmarkViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalBookmarkViewHolder {
        return FestivalBookmarkViewHolder.of(
            parent = parent,
        )
    }

    override fun onBindViewHolder(holder: FestivalBookmarkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FestivalBookmarkItemUiState>() {
            override fun areItemsTheSame(
                oldItem: FestivalBookmarkItemUiState,
                newItem: FestivalBookmarkItemUiState,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FestivalBookmarkItemUiState,
                newItem: FestivalBookmarkItemUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
