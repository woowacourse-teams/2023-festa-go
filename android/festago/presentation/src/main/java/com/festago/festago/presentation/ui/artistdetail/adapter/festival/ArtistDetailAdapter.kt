package com.festago.festago.presentation.ui.artistdetail.festival

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistDetailUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.StageUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalTabUiState

class ArtistDetailAdapter : ListAdapter<Any, ArtistDetailViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistDetailViewHolder {
        return when (viewType) {
            1 -> ArtistDetailFestivalViewHolder.of(parent)
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: ArtistDetailViewHolder, position: Int) {
        val item = getItem(position)
        return when (holder) {
            is ArtistDetailFestivalViewHolder -> holder.bind(item as StageUiState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is StageUiState -> 1
            else -> throw IllegalArgumentException("Invalid item")
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is StageUiState && newItem is StageUiState -> oldItem.id == newItem.id
                else -> false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem is StageUiState && newItem is StageUiState
                -> oldItem as StageUiState == newItem

                else -> false
            }
        }
    }
}
