package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.LayoutInflater
import android.view.ViewGroup
import com.festago.festago.presentation.databinding.ItemFestivalListPopularBinding
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.PopularFestivalViewPagerAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalUiState
import com.google.android.material.tabs.TabLayoutMediator

class FestivalListPopularViewHolder(val binding: ItemFestivalListPopularBinding) :
    FestivalListViewHolder(binding) {
    private val popularFestivalViewPager: PopularFestivalViewPagerAdapter =
        PopularFestivalViewPagerAdapter(
            foregroundViewPager = binding.vpPopularFestivalForeground,
            backgroundViewPager = binding.vpPopularFestivalBackground,
        ) { item ->
            binding.item = item
            binding.tvPopularFestivalArtistsName.text =
                item.artists.joinToString(ARTIST_NAME_JOIN_SEPARATOR) { it.name }
        }

    init {
        TabLayoutMediator(
            binding.tlDotIndicator,
            binding.vpPopularFestivalBackground,
        ) { tab, position ->
            tab.view.isClickable = false
        }.attach()
    }

    fun bind(popularFestivalUiState: PopularFestivalUiState) {
        binding.tvPopularFestivalTitle.text = popularFestivalUiState.title
        popularFestivalViewPager.submitList(popularFestivalUiState.festivals)
    }

    companion object {
        private const val ARTIST_NAME_JOIN_SEPARATOR = ", "

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
