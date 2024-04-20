package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.LayoutInflater
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemFestivalListMoreItemBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalMoreItemUiState

class FestivalListMoreItemViewHolder(val binding: ItemFestivalListMoreItemBinding) :
    FestivalListViewHolder(binding) {

    fun bind(festivalMoreItemUiState: FestivalMoreItemUiState) {
        festivalMoreItemUiState.requestMoreItem()
    }

    companion object {
        fun of(parent: ViewGroup): FestivalListMoreItemViewHolder {
            val binding = ItemFestivalListMoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalListMoreItemViewHolder(binding)
        }
    }
}
