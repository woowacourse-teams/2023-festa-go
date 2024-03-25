package com.festago.festago.presentation.ui.search.screen

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SearchScreenAdapter : ListAdapter<ItemSearchScreenUiState, SearchScreenViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchScreenViewHolder {
        return when (viewType) {
            1 -> FestivalSearchScreenViewHolder.of(parent)
            2 -> ArtistSearchScreenViewHolder.of(parent)
            3 -> SchoolSearchScreenViewHolder.of(parent)
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: SearchScreenViewHolder, position: Int) {
        val item = getItem(position)
        return when (holder) {
            is FestivalSearchScreenViewHolder -> holder.bind(item as ItemSearchScreenUiState.FestivalSearchScreen)
            is ArtistSearchScreenViewHolder -> holder.bind(item as ItemSearchScreenUiState.ArtistSearchScreen)
            is SchoolSearchScreenViewHolder -> holder.bind(item as ItemSearchScreenUiState.SchoolSearchScreen)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ItemSearchScreenUiState.FestivalSearchScreen -> 1
            is ItemSearchScreenUiState.ArtistSearchScreen -> 2
            is ItemSearchScreenUiState.SchoolSearchScreen -> 3
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemSearchScreenUiState>() {
            override fun areItemsTheSame(
                oldItem: ItemSearchScreenUiState,
                newItem: ItemSearchScreenUiState,
            ): Boolean = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: ItemSearchScreenUiState,
                newItem: ItemSearchScreenUiState,
            ): Boolean = oldItem == newItem
        }
    }
}
