package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.LayoutInflater
import android.view.ViewGroup
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemFestivalListEmptyItemBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalEmptyItemUiState

class FestivalListEmptyItemViewHolder(
    val binding: ItemFestivalListEmptyItemBinding,
) : FestivalListViewHolder(binding) {

    fun bind(festivalEmptyItemState: FestivalEmptyItemUiState) {
        val emptyText = when (festivalEmptyItemState.tabPosition) {
            0 -> binding.root.context.getString(R.string.festival_list_tv_festivals_empty_progress)

            1 -> binding.root.context.getString(R.string.festival_list_tv_festivals_empty_upcoming)

            else -> binding.root.context.getString(R.string.festival_list_tv_festivals_empty)
        }
        binding.tvFestivalsEmpty.text = emptyText
    }

    companion object {
        fun of(parent: ViewGroup): FestivalListEmptyItemViewHolder {
            val binding = ItemFestivalListEmptyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalListEmptyItemViewHolder(binding)
        }
    }
}
