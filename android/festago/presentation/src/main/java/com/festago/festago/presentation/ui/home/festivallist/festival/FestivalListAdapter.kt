package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class FestivalListAdapter : ListAdapter<Any, FestivalListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivalListViewHolder {
        return FestivalListViewHolder.of(parent, viewType)
    }

    override fun onBindViewHolder(holder: FestivalListViewHolder, position: Int) {
        return FestivalListViewHolder.bind(holder, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return FestivalListViewHolder.viewTypeOf(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(
                oldItem: Any,
                newItem: Any,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Any,
                newItem: Any,
            ): Boolean {
                return false
            }
        }
    }
}
