package com.festago.festago.presentation.ui.search.recentsearch

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.search.uistate.RecentSearchItemUiState

class RecentSearchAdapter : ListAdapter<RecentSearchItemUiState, RecentSearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        return RecentSearchViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<RecentSearchItemUiState>() {
            override fun areItemsTheSame(
                oldItem: RecentSearchItemUiState,
                newItem: RecentSearchItemUiState,
            ): Boolean {
                return oldItem.recentQuery == newItem.recentQuery
            }

            override fun areContentsTheSame(
                oldItem: RecentSearchItemUiState,
                newItem: RecentSearchItemUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
