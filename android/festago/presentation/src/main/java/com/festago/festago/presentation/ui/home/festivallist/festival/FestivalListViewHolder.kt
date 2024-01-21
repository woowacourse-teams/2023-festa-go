package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState

sealed class FestivalListViewHolder(binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun viewTypeOf(item: Any): Int {
            return when (item) {
                is FestivalListUiState -> 1
                is FestivalItemUiState -> 2
                else -> throw IllegalArgumentException("Invalid item")
            }
        }

        fun of(parent: ViewGroup, viewType: Int): FestivalListViewHolder {
            return when (viewType) {
                1 -> FestivalListPopularViewHolder.of(parent)
                2 -> FestivalListFestivalViewHolder.of(parent)
                else -> throw IllegalArgumentException("Invalid viewType")
            }
        }

        fun bind(holder: FestivalListViewHolder, item: Any) {
            when (holder) {
                is FestivalListPopularViewHolder -> holder.bind((item as FestivalListUiState.Success).festivals)
                is FestivalListFestivalViewHolder -> holder.bind(item as FestivalItemUiState)
            }
        }
    }
}
