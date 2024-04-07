package com.festago.festago.presentation.ui.search.schoolSearchAdatper

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.search.uistate.SchoolSearchItemUiState

class SchoolSearchAdapter : ListAdapter<SchoolSearchItemUiState, SchoolSearchViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolSearchViewHolder {
        return SchoolSearchViewHolder.of(parent)
    }

    override fun onBindViewHolder(holder: SchoolSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<SchoolSearchItemUiState>() {
            override fun areItemsTheSame(
                oldItem: SchoolSearchItemUiState,
                newItem: SchoolSearchItemUiState,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SchoolSearchItemUiState,
                newItem: SchoolSearchItemUiState,
            ): Boolean = oldItem == newItem
        }
    }
}
