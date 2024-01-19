package com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalItemUiState

class PopularFestivalForegroundAdapter :
    ListAdapter<PopularFestivalItemUiState, PopularFestivalForegroundViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFestivalForegroundViewHolder {
        return PopularFestivalForegroundViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: PopularFestivalForegroundViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PopularFestivalItemUiState>() {
            override fun areItemsTheSame(
                oldItem: PopularFestivalItemUiState,
                newItem: PopularFestivalItemUiState,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PopularFestivalItemUiState,
                newItem: PopularFestivalItemUiState,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
