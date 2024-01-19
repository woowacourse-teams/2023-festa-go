package com.festago.festago.presentation.ui.home.festivallist.popularfestival

import android.content.Context
import androidx.viewpager2.widget.ViewPager2
import com.festago.festago.presentation.R
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.background.PopularFestivalBackgroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.foreground.PopularFestivalForegroundAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalItemUiState

class PopularFestivalViewPagerAdapter(
    context: Context,
    private val foregroundViewPager: ViewPager2,
    private val backgroundViewPager: ViewPager2,
) {

    private val foregroundAdapter: PopularFestivalForegroundAdapter =
        PopularFestivalForegroundAdapter()
    private val backgroundAdapter: PopularFestivalBackgroundAdapter =
        PopularFestivalBackgroundAdapter()

    init {
        foregroundViewPager.adapter = foregroundAdapter
        backgroundViewPager.adapter = backgroundAdapter

        initFrontViewPager(context)
        backgroundViewPager.isUserInputEnabled = false
    }

    private fun initFrontViewPager(context: Context) {
        narrowSpaceViewPager(
            foregroundViewPager,
            context.resources.getDimensionPixelOffset(R.dimen.offsetBetweenPages).toFloat(),
        )

        foregroundViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                backgroundViewPager.setCurrentItem(position, false)
            }
        })
    }

    private fun narrowSpaceViewPager(
        viewPager: ViewPager2,
        offsetBetweenPages: Float,
    ) {
        viewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * offsetBetweenPages)
            page.translationX = offset
        }
    }

    private fun setViewPagersOffscreenLimit(limit: Int) {
        foregroundViewPager.offscreenPageLimit = limit
        backgroundViewPager.offscreenPageLimit = limit
    }

    fun submitList(popularFestivals: List<PopularFestivalItemUiState>) {
        setViewPagersOffscreenLimit(popularFestivals.size)
        foregroundAdapter.submitList(popularFestivals)
        backgroundAdapter.submitList(popularFestivals)
    }
}
