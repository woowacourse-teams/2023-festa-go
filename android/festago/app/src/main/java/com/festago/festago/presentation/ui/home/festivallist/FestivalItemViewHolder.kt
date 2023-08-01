package com.festago.festago.presentation.ui.home.festivallist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemFestivalListBinding
import com.festago.festago.presentation.model.FestivalUiModel

class FestivalItemViewHolder(
    private val binding: ItemFestivalListBinding,
    vm: FestivalListViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.vm = vm
    }

    fun bind(item: FestivalUiModel) {
        binding.festival = item
    }

    companion object {
        fun of(parent: ViewGroup, vm: FestivalListViewModel): FestivalItemViewHolder {
            val binding = ItemFestivalListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalItemViewHolder(binding, vm)
        }
    }
}
