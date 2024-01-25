package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.LayoutInflater
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemFestivalListPopularBinding
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.PopularFestivalViewPagerAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalListUiState
import com.google.android.material.tabs.TabLayoutMediator

class FestivalListPopularViewHolder(val binding: ItemFestivalListPopularBinding) :
    FestivalListViewHolder(binding) {
    private val popularFestivalViewPager: PopularFestivalViewPagerAdapter =
        PopularFestivalViewPagerAdapter(
            foregroundViewPager = binding.vpPopularFestivalForeground,
            backgroundViewPager = binding.vpPopularFestivalBackground,
        )

    init {
        TabLayoutMediator(
            binding.tlDotIndicator,
            binding.vpPopularFestivalForeground,
        ) { tab, position -> }.attach()
    }

    fun bind(festivals: List<FestivalItemUiState>) {
        popularFestivalViewPager.submitList(festivals)
        binding.uiState = FestivalListUiState.Success(
            popularFestivals = festivals,
            festivals = festivals,
        )
    }

    companion object {
        fun of(parent: ViewGroup): FestivalListPopularViewHolder {
            val binding = ItemFestivalListPopularBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalListPopularViewHolder(binding)
        }
    }
}
