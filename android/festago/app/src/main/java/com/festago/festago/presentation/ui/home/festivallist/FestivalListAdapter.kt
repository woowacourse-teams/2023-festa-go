package com.festago.festago.presentation.ui.home.festivallist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.FestivalUiModel

class FestivalListAdapter : ListAdapter<FestivalUiModel, FestivalItemViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalItemViewHolder {
        return FestivalItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FestivalItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FestivalUiModel>() {
            override fun areItemsTheSame(
                oldItem: FestivalUiModel,
                newItem: FestivalUiModel,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FestivalUiModel,
                newItem: FestivalUiModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
