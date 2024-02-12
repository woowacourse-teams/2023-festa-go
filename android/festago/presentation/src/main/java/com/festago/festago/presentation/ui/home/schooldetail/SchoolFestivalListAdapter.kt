package com.festago.festago.presentation.ui.home.schooldetail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.home.festivallist.festival.FestivalListFestivalViewHolder
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

class SchoolFestivalListAdapter :
    ListAdapter<FestivalItemUiState, FestivalListFestivalViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FestivalListFestivalViewHolder {
        return FestivalListFestivalViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: FestivalListFestivalViewHolder, position: Int) {
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
