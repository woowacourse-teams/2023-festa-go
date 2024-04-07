package com.festago.festago.presentation.ui.search.artistsearch

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemSearchArtistBinding
import com.festago.festago.presentation.ui.search.uistate.ArtistSearchItemUiState

class ArtistSearchViewHolder(
    private val binding: ItemSearchArtistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ArtistSearchItemUiState) {
        binding.item = item
        binding.tvTodayStageCount.setStageCount(
            count = item.todayStage,
            stringRes = R.string.search_artist_tv_today_stage_count,
        )
        binding.tvUpcomingStageCount.setStageCount(
            item.upcomingStage,
            stringRes = R.string.search_artist_tv_upcoming_stage_count,
        )
    }

    private fun TextView.setStageCount(count: Int, @StringRes stringRes: Int) {
        val stageCountText = context.getString(stringRes, count)
        text = SpannableString(stageCountText).apply {
            getPartialColorText(
                start = COLOR_INDEX,
                end = COLOR_INDEX + count.toString().length,
                color = context.getColor(R.color.secondary_pink_01),
            )
        }
    }

    private fun SpannableString.getPartialColorText(
        start: Int,
        end: Int,
        @ColorInt color: Int,
    ) {
        setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    companion object {
        private const val COLOR_INDEX = 6

        fun of(parent: ViewGroup): ArtistSearchViewHolder {
            val binding = ItemSearchArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ArtistSearchViewHolder(binding)
        }
    }
}
