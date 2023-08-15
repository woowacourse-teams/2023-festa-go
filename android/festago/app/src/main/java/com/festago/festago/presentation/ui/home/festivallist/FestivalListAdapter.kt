package com.festago.festago.presentation.ui.home.festivallist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.FestivalItemUiState

class FestivalListAdapter : ListAdapter<FestivalItemUiState, FestivalItemViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalItemViewHolder {
        return FestivalItemViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: FestivalItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FestivalItemUiState>() {
            override fun areItemsTheSame(
                oldItem: FestivalItemUiState,
                newItem: FestivalItemUiState,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FestivalItemUiState,
                newItem: FestivalItemUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
