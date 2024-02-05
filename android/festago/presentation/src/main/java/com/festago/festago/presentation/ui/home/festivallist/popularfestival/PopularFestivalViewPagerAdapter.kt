package com.festago.festago.presentation.ui.home.festivallist.popularfestival

import android.content.res.Resources
import androidx.viewpager2.widget.ViewPager2
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.background.PopularFestivalBackgroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground.PopularFestivalForegroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import kotlin.math.abs

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
                onPopularFestivalSelected(popularFestivals[position])
            }
        }
        viewpager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun narrowSpaceViewPager(viewPager: ViewPager2) {
        viewPager.setPageTransformer { page, position ->
            val offsetBetweenPages =
                Resources.getSystem().configuration.screenWidthDp - IMAGE_SIZE - INTERVAL_IMAGE + (IMAGE_SIZE - (IMAGE_SIZE * RATE_SELECT_BY_UNSELECT)) * 0.5f
            val offset = position * -dpToPx(offsetBetweenPages)
            page.translationX = offset

            if (position <= ALREADY_LOAD_POSITION_CONDITION) {
                val scaleFactor = RATE_SELECT_BY_UNSELECT.coerceAtLeast(1 - abs(position))
                page.scaleY = scaleFactor
                page.scaleX = scaleFactor
            }
        }
    }

    private fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

    private fun setOffscreenPagesLimit(viewPager: ViewPager2, limit: Int) {
        viewPager.offscreenPageLimit = limit
    }

    fun submitList(popularFestivals: List<FestivalItemUiState>) {
        foregroundAdapter.submitList(popularFestivals)
        backgroundAdapter.submitList(popularFestivals)
    }

    companion object {
        private const val ALREADY_LOAD_POSITION_CONDITION = 2
        private const val RATE_SELECT_BY_UNSELECT = 0.81f
        private const val PAGE_LIMIT = 4
        private const val IMAGE_SIZE = 220.0f
        private const val INTERVAL_IMAGE = 24.0f
    }
}
