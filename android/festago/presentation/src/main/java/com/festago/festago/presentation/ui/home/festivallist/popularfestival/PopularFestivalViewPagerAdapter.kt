package com.festago.festago.presentation.ui.home.festivallist.popularfestival

import androidx.viewpager2.widget.ViewPager2
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.background.PopularFestivalBackgroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground.PopularFestivalForegroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

class PopularFestivalViewPagerAdapter(
    foregroundViewPager: ViewPager2,
    backgroundViewPager: ViewPager2,
) {

    private val foregroundAdapter: PopularFestivalForegroundAdapter =
        PopularFestivalForegroundAdapter()
    private val backgroundAdapter: PopularFestivalBackgroundAdapter =
        PopularFestivalBackgroundAdapter()

    init {
        foregroundViewPager.adapter = foregroundAdapter
        backgroundViewPager.adapter = backgroundAdapter

        setTargetItemOnPageSelected(viewpager = foregroundViewPager, target = backgroundViewPager)
        narrowSpaceViewPager(viewPager = foregroundViewPager)
        setOffscreenPagesLimit(foregroundViewPager, PAGE_LIMIT)
        setOffscreenPagesLimit(backgroundViewPager, PAGE_LIMIT)
        backgroundViewPager.isUserInputEnabled = false
    }

    private fun setTargetItemOnPageSelected(viewpager: ViewPager2, target: ViewPager2) {
        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                target.setCurrentItem(position, false)
            }
        }
        viewpager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun narrowSpaceViewPager(viewPager: ViewPager2) {
        viewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * OFFSET_BETWEEN_PAGES)
            page.translationX = offset
        }
    }

    private fun setOffscreenPagesLimit(viewPager: ViewPager2, limit: Int) {
        viewPager.offscreenPageLimit = limit
    }

    fun submitList(popularFestivals: List<FestivalItemUiState>) {
        foregroundAdapter.submitList(popularFestivals)
        backgroundAdapter.submitList(popularFestivals)
    }

    companion object {
        private const val PAGE_LIMIT = 3
        private const val OFFSET_BETWEEN_PAGES = 80.0f
    }
}
