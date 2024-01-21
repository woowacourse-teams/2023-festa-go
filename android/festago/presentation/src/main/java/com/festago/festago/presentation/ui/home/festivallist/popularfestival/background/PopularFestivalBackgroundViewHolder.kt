package com.festago.festago.presentation.ui.home.festivallist.popularfestival.background

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.databinding.ItemPopularFestivalBackgroundBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

class PopularFestivalBackgroundViewHolder(
    private val binding: ItemPopularFestivalBackgroundBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FestivalItemUiState) {
        binding.item = item
    }

    companion object {
        fun of(parent: ViewGroup): PopularFestivalBackgroundViewHolder {
            val binding = ItemPopularFestivalBackgroundBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return PopularFestivalBackgroundViewHolder(binding)
        }
    }
}
