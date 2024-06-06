package com.festago.festago.presentation.ui.home.festivallist.festival

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.ui.home.festivallist.uistate.ArtistUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalEmptyItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalMoreItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalTabUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalUiState

class FestivalListAdapter(
    private val onArtistClick: (ArtistUiState) -> Unit,
) : ListAdapter<Any, RecyclerView.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> FestivalListPopularViewHolder2.of(parent)
            2 -> FestivalListFestivalViewHolder.of(parent, onArtistClick)
            3 -> FestivalListTabViewHolder.of(parent)
            4 -> FestivalListMoreItemViewHolder.of(parent)
            5 -> FestivalListEmptyItemViewHolder.of(parent)
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        return when (holder) {
            is FestivalListPopularViewHolder2 -> holder.bind(item as PopularFestivalUiState)
            is FestivalListFestivalViewHolder -> holder.bind(item as FestivalItemUiState)
            is FestivalListTabViewHolder -> holder.bind(item as FestivalTabUiState)
            is FestivalListMoreItemViewHolder -> holder.bind(item as FestivalMoreItemUiState)
            is FestivalListEmptyItemViewHolder -> holder.bind(item as FestivalEmptyItemUiState)
            else -> throw IllegalArgumentException("Invalid holder")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PopularFestivalUiState -> 1
            is FestivalItemUiState -> 2
            is FestivalTabUiState -> 3
            is FestivalMoreItemUiState -> 4
            is FestivalEmptyItemUiState -> 5
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
                oldItem is FestivalEmptyItemUiState && newItem is FestivalEmptyItemUiState -> oldItem.tabPosition == newItem.tabPosition
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

                oldItem is FestivalEmptyItemUiState && newItem is FestivalEmptyItemUiState -> oldItem as FestivalEmptyItemUiState == newItem
                else -> false
            }
        }
    }
}
