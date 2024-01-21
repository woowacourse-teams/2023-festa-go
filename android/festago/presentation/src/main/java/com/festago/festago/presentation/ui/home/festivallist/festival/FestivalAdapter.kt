package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

class FestivalAdapter : ListAdapter<FestivalItemUiState, FestivalViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalViewHolder {
        return FestivalViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: FestivalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FestivalItemUiState>() {
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
