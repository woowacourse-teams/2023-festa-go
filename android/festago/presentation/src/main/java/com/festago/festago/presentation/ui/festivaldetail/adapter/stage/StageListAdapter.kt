package com.festago.festago.presentation.ui.festivaldetail.adapter.stage

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.festivaldetail.uiState.StageItemUiState

class StageListAdapter : ListAdapter<StageItemUiState, StageViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder {
        return StageViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: StageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<StageItemUiState>() {
            override fun areItemsTheSame(
                oldItem: StageItemUiState,
                newItem: StageItemUiState,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StageItemUiState,
                newItem: StageItemUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
