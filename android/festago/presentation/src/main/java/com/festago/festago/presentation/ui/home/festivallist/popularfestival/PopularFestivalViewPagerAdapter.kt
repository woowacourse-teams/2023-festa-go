package com.festago.festago.presentation.ui.home.festivallist.popularfestival

import android.content.res.Resources
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.background.PopularFestivalBackgroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground.PopularFestivalForegroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import kotlin.math.abs

class PopularFestivalViewPagerAdapter(
    private val foregroundViewPager: ViewPager2,
    backgroundViewPager: ViewPager2,
    private val onPopularFestivalSelected: (FestivalItemUiState) -> Unit,
) {

    private val popularFestivals = mutableListOf<FestivalItemUiState>()
    private val foregroundAdapter: PopularFestivalForegroundAdapter =
        PopularFestivalForegroundAdapter()
    private val backgroundAdapter: PopularFestivalBackgroundAdapter =
        PopularFestivalBackgroundAdapter()

    init {
        foregroundViewPager.adapter = foregroundAdapter
        backgroundViewPager.adapter = backgroundAdapter

        setTargetItemOnPageSelected(viewPager = foregroundViewPager, target = backgroundViewPager)
        narrowSpaceViewPager(viewPager = foregroundViewPager)
        setOffscreenPagesLimit(foregroundViewPager, PAGE_LIMIT)
        setOffscreenPagesLimit(backgroundViewPager, PAGE_LIMIT)
        backgroundViewPager.isUserInputEnabled = false
    }

    private fun setTargetItemOnPageSelected(viewPager: ViewPager2, target: ViewPager2) {
        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val itemIndex = position % popularFestivals.size
                target.setCurrentItem(itemIndex, false)
                onPopularFestivalSelected(popularFestivals[itemIndex])
            }
        }
        viewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun narrowSpaceViewPager(viewPager: ViewPager2) {
        viewPager.setPageTransformer { page, position ->
            translateOffsetBetweenPages(position, page)
            reduceUnselectedPagesScale(page, position)
        }
    }

    private fun translateOffsetBetweenPages(position: Float, page: View) {
        val screenWidth = Resources.getSystem().configuration.screenWidthDp
        val offsetBetweenPages = screenWidth - IMAGE_SIZE - INTERVAL_IMAGE
        val reducedDifference = IMAGE_SIZE - (IMAGE_SIZE * RATE_SELECT_BY_UNSELECT)
        val offset = position * -dpToPx(offsetBetweenPages + reducedDifference * 0.5f)
        page.translationX = offset
    }

    private fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

    private fun reduceUnselectedPagesScale(page: View, position: Float) {
        page.movePivotTo(x = page.pivotX, y = MIDDLE_IMAGE_PIVOT)
        if (position <= ALREADY_LOAD_POSITION_CONDITION) {
            page.reduceScaleBy(position = position, rate = RATE_SELECT_BY_UNSELECT)
        }
    }

    private fun View.movePivotTo(x: Float, y: Float) {
        pivotX = x
        pivotY = y
    }

    private fun View.reduceScaleBy(position: Float, rate: Float) {
        val scaleFactor = rate.coerceAtLeast(1 - abs(position))
        scaleY = scaleFactor
        scaleX = scaleFactor
    }

    private fun setOffscreenPagesLimit(viewPager: ViewPager2, limit: Int) {
        viewPager.offscreenPageLimit = limit
    }

    fun submitList(festivals: List<FestivalItemUiState>) {
        popularFestivals.clear()
        popularFestivals.addAll(festivals)
        foregroundAdapter.submitList(festivals)
        backgroundAdapter.submitList(festivals)
        val initialPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % popularFestivals.size)
        foregroundViewPager.setCurrentItem(initialPosition, false)
    }

    companion object {
        private const val ALREADY_LOAD_POSITION_CONDITION = 2
        private const val RATE_SELECT_BY_UNSELECT = 0.81f
        private const val PAGE_LIMIT = 4
        private const val IMAGE_SIZE = 220.0f
        private const val INTERVAL_IMAGE = 24.0f
        private const val MIDDLE_IMAGE_PIVOT = 360.0f
    }
}
