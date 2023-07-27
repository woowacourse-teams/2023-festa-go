package com.festago.festago.presentation.ui.home.festivallist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemFestivalBinding
import com.festago.festago.presentation.model.FestivalUiModel

class FestivalItemViewHolder(
    private val binding: ItemFestivalBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FestivalUiModel) {
        binding.festival = item
    }

    companion object {
        fun from(parent: ViewGroup): FestivalItemViewHolder {
            val binding = ItemFestivalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalItemViewHolder(binding)
        }
    }
}
