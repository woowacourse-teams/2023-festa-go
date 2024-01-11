package com.festago.festago.presentation.ui.home.festivallist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemFestivalListBinding

class FestivalItemViewHolder(
    private val binding: ItemFestivalListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FestivalItemUiState) {
        binding.festival = item
    }

    companion object {
        fun of(parent: ViewGroup): FestivalItemViewHolder {
            val binding = ItemFestivalListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalItemViewHolder(binding)
        }
    }
}
