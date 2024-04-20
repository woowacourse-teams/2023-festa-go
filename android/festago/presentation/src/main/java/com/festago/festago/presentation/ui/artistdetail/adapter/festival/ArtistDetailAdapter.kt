package com.festago.festago.presentation.ui.artistdetail.adapter.festival

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.MoreItemUiState

class ArtistDetailAdapter : ListAdapter<Any, ArtistDetailViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistDetailViewHolder {
        return when (viewType) {
            1 -> ArtistDetailFestivalViewHolder.of(parent)
            2 -> ArtistDetailMoreItemViewHolder.of(parent)
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: ArtistDetailViewHolder, position: Int) {
        val item = getItem(position)
        return when (holder) {
            is ArtistDetailFestivalViewHolder -> holder.bind(item as FestivalItemUiState)
            is ArtistDetailMoreItemViewHolder -> holder.bind(item as MoreItemUiState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FestivalItemUiState -> 1
            is MoreItemUiState -> 2
            else -> throw IllegalArgumentException("Invalid Item")
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is FestivalItemUiState && newItem is FestivalItemUiState -> oldItem.id == newItem.id
                oldItem is MoreItemUiState && newItem is MoreItemUiState -> true
                else -> false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is FestivalItemUiState && newItem is FestivalItemUiState
                -> oldItem as FestivalItemUiState == newItem

                oldItem is MoreItemUiState && newItem is MoreItemUiState -> true

                else -> false
            }

        }
    }
}
