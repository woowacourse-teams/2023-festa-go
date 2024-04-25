package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SchoolBookmarkAdapter : ListAdapter<SchoolBookmarkUiState, SchoolBookmarkViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolBookmarkViewHolder {
        return SchoolBookmarkViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: SchoolBookmarkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<SchoolBookmarkUiState>() {
            override fun areItemsTheSame(
                oldItem: SchoolBookmarkUiState,
                newItem: SchoolBookmarkUiState,
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: SchoolBookmarkUiState,
                newItem: SchoolBookmarkUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
