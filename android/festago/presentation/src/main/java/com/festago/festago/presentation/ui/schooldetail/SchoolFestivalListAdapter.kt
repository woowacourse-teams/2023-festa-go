package com.festago.festago.presentation.ui.schooldetail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.schooldetail.uistate.FestivalItemUiState

class SchoolFestivalListAdapter :
    ListAdapter<FestivalItemUiState, SchoolDetailFestivalViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SchoolDetailFestivalViewHolder {
        return SchoolDetailFestivalViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: SchoolDetailFestivalViewHolder, position: Int) {
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
