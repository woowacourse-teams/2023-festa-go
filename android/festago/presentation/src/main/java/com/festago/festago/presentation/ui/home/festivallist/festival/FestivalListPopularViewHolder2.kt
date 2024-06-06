package com.festago.festago.presentation.ui.home.festivallist.festival

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.ui.home.festivallist.popularfestival.PopularFestivalPager
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalUiState

class FestivalListPopularViewHolder2(
    private val composeView: ComposeView
) : RecyclerView.ViewHolder(composeView) {

    @RequiresApi(Build.VERSION_CODES.S)
    fun bind(item: PopularFestivalUiState) {
        composeView.setContent {
            PopularFestivalPager(item)
        }
    }

    companion object {
        fun of(parent: ViewGroup): FestivalListPopularViewHolder2 {
            val composeView = ComposeView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            return FestivalListPopularViewHolder2(composeView)
        }
    }
}
