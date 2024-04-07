package com.festago.festago.presentation.ui.search.recentsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemRecentSearchBinding
import com.festago.festago.presentation.ui.search.uistate.RecentSearchItemUiState

class RecentSearchViewHolder(
    val binding: ItemRecentSearchBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecentSearchItemUiState) {
        binding.item = item
    }

    companion object {
        fun of(parent: ViewGroup): RecentSearchViewHolder {
            val binding = ItemRecentSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return RecentSearchViewHolder(binding)
        }
    }
}
