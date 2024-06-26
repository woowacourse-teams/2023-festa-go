package com.festago.festago.presentation.ui.home.festivallist.festival

import android.view.LayoutInflater
import android.view.ViewGroup
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemFestivalListTabBinding
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalFilterUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalTabUiState
import com.google.android.material.tabs.TabLayout

class FestivalListTabViewHolder(val binding: ItemFestivalListTabBinding) :
    FestivalListViewHolder(binding) {
    init {
        binding.tlFestivalListTab.addTab(
            binding.tlFestivalListTab.newTab().setText(
                binding.root.context.getText(R.string.festival_list_ti_current_festival),
            ),
        )
        binding.tlFestivalListTab.addTab(
            binding.tlFestivalListTab.newTab().setText(
                binding.root.context.getText(R.string.festival_list_ti_upcoming_festival),
            ),
        )
    }

    fun bind(festivalTabUiState: FestivalTabUiState) {
        with(binding.tlFestivalListTab) {
            clearOnTabSelectedListeners()
            selectTab(getTabAt(festivalTabUiState.selectedFilter.tabPosition))
            addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        festivalTabUiState.onFilterSelected(getFestivalFilterAt(tab.position))
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) = Unit
                    override fun onTabReselected(tab: TabLayout.Tab) = Unit
                },
            )
        }
        binding.schoolRegion = festivalTabUiState.selectedRegion
        binding.tvRegion.setOnClickListener {
            festivalTabUiState.onRegionClick.invoke()
        }
        if (festivalTabUiState.selectedRegion == null) {
            binding.tvRegion.isSelected = false
            binding.tvRegion.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_pin_normal, 0, 0, 0
            )
        } else {
            binding.tvRegion.isSelected = true
            binding.tvRegion.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_pin_select, 0, 0, 0
            )
        }
    }

    private fun getFestivalFilterAt(position: Int): FestivalFilterUiState =
        FestivalFilterUiState.values().find { it.tabPosition == position }
            ?: FestivalFilterUiState.PROGRESS

    companion object {
        fun of(parent: ViewGroup): FestivalListTabViewHolder {
            val binding = ItemFestivalListTabBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalListTabViewHolder(binding)
        }
    }
}
