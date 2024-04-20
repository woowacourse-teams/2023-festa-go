package com.festago.festago.presentation.ui.schooldetail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.schooldetail.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.MoreItemUiState

class SchoolFestivalListAdapter : ListAdapter<Any, SchoolDetailViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SchoolDetailViewHolder {
        return when (viewType) {
            1 -> SchoolDetailFestivalViewHolder.of(parent)
            2 -> SchoolDetailMoreItemViewHolder.of(parent)
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: SchoolDetailViewHolder, position: Int) {
        val item = getItem(position)
        return when (holder) {
            is SchoolDetailFestivalViewHolder -> holder.bind(item as FestivalItemUiState)
            is SchoolDetailMoreItemViewHolder -> holder.bind(item as MoreItemUiState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FestivalItemUiState -> 1
            is MoreItemUiState -> 2
            else -> throw IllegalArgumentException("Invalid item")
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Any>() {
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
