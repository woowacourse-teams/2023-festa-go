package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalMoreItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalTabUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalUiState

class FestivalListAdapter(
    private val onArtistClick: (Long) -> Unit,
) : ListAdapter<Any, FestivalListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalListViewHolder {
        return when (viewType) {
            1 -> FestivalListPopularViewHolder.of(parent)
            2 -> FestivalListFestivalViewHolder.of(parent, onArtistClick)
            3 -> FestivalListTabViewHolder.of(parent)
            4 -> FestivalListMoreItemViewHolder.of(parent)
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: FestivalListViewHolder, position: Int) {
        val item = getItem(position)
        return when (holder) {
            is FestivalListPopularViewHolder -> holder.bind(item as PopularFestivalUiState)
            is FestivalListFestivalViewHolder -> holder.bind(item as FestivalItemUiState)
            is FestivalListTabViewHolder -> holder.bind(item as FestivalTabUiState)
            is FestivalListMoreItemViewHolder -> holder.bind(item as FestivalMoreItemUiState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PopularFestivalUiState -> 1
            is FestivalItemUiState -> 2
            is FestivalTabUiState -> 3
            is FestivalMoreItemUiState -> 4
            else -> throw IllegalArgumentException("Invalid item")
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is PopularFestivalUiState && newItem is PopularFestivalUiState -> true
                oldItem is FestivalItemUiState && newItem is FestivalItemUiState -> oldItem.id == newItem.id
                oldItem is FestivalTabUiState && newItem is FestivalTabUiState -> oldItem.selectedRegion == newItem.selectedRegion
                oldItem is FestivalMoreItemUiState && newItem is FestivalMoreItemUiState -> true
                else -> false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is PopularFestivalUiState && newItem is PopularFestivalUiState
                -> oldItem as PopularFestivalUiState == newItem

                oldItem is FestivalItemUiState && newItem is FestivalItemUiState
                -> oldItem as FestivalItemUiState == newItem

                oldItem is FestivalTabUiState && newItem is FestivalTabUiState
                -> oldItem as FestivalTabUiState == newItem

                oldItem is FestivalMoreItemUiState && newItem is FestivalMoreItemUiState -> true

                else -> false
            }
        }
    }
}
