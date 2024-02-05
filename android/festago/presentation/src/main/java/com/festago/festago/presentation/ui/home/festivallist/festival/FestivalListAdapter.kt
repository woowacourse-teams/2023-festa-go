package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalTabUiState

class FestivalListAdapter(
    private val onArtistClick: (Long) -> Unit,
) : ListAdapter<Any, FestivalListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalListViewHolder {
        return when (viewType) {
            1 -> FestivalListPopularViewHolder.of(parent)
            2 -> FestivalListFestivalViewHolder.of(parent, onArtistClick)
            3 -> FestivalListTabViewHolder.of(parent)
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: FestivalListViewHolder, position: Int) {
        val item = getItem(position)
        return when (holder) {
            is FestivalListPopularViewHolder -> holder.bind((item as FestivalListUiState.Success).popularFestivals)
            is FestivalListFestivalViewHolder -> holder.bind(item as FestivalItemUiState)
            is FestivalListTabViewHolder -> holder.bind(item as FestivalTabUiState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FestivalListUiState -> 1
            is FestivalItemUiState -> 2
            is FestivalTabUiState -> 3
            else -> throw IllegalArgumentException("Invalid item")
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is FestivalListUiState && newItem is FestivalListUiState -> true
                oldItem is FestivalItemUiState && newItem is FestivalItemUiState -> oldItem.id == newItem.id
                oldItem is FestivalTabUiState && newItem is FestivalTabUiState -> true
                else -> false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is FestivalListUiState && newItem is FestivalListUiState
                -> oldItem as FestivalListUiState == newItem

                oldItem is FestivalItemUiState && newItem is FestivalItemUiState
                -> oldItem as FestivalItemUiState == newItem

                oldItem is FestivalTabUiState && newItem is FestivalTabUiState
                -> oldItem as FestivalTabUiState == newItem

                else -> false
            }
        }
    }
}
