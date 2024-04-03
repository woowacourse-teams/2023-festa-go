package com.festago.festago.presentation.ui.search.festivalsearch

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.search.uistate.FestivalSearchItemUiState

class FestivalSearchAdapter :
    ListAdapter<FestivalSearchItemUiState, FestivalSearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalSearchViewHolder {
        return FestivalSearchViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: FestivalSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<FestivalSearchItemUiState>() {
            override fun areItemsTheSame(
                oldItem: FestivalSearchItemUiState,
                newItem: FestivalSearchItemUiState,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: FestivalSearchItemUiState,
                newItem: FestivalSearchItemUiState,
            ): Boolean = oldItem == newItem
        }
    }
}
