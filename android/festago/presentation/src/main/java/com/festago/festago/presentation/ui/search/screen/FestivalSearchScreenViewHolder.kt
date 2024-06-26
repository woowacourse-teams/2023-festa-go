package com.festago.festago.presentation.ui.search.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemFestivalSearchScreenBinding
import com.festago.festago.presentation.ui.search.festivalsearch.FestivalSearchAdapter

class FestivalSearchScreenViewHolder(
    private val binding: ItemFestivalSearchScreenBinding,
) : SearchScreenViewHolder(binding) {

    private val festivalSearchAdapter: FestivalSearchAdapter = FestivalSearchAdapter()

    init {
        binding.rvFestivals.adapter = festivalSearchAdapter
    }

    fun bind(item: ItemSearchScreenUiState.FestivalSearchScreen) {
        festivalSearchAdapter.submitList(item.festivalSearches)
        setNoResultVisibility(item)
        binding.btnFestago.setOnClickListener {
            item.onAddSearchQueryClick()
        }
    }

    private fun setNoResultVisibility(item: ItemSearchScreenUiState.FestivalSearchScreen) {
        val visibility = if (item.festivalSearches.isEmpty()) View.VISIBLE else View.GONE
        binding.tvNoResult.visibility = visibility
        binding.tvNoResultGuide.visibility = visibility
        binding.btnFestago.visibility = visibility
    }

    companion object {
        fun of(parent: ViewGroup): FestivalSearchScreenViewHolder {
            val binding = ItemFestivalSearchScreenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalSearchScreenViewHolder(binding)
        }
    }
}
