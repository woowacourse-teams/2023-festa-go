package com.festago.festago.presentation.ui.search.festivalsearch

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemSearchFestivalBinding
import com.festago.festago.presentation.ui.search.festivalsearch.artist.ArtistAdapter
import com.festago.festago.presentation.ui.search.uistate.FestivalSearchItemUiState
import java.time.LocalDate

class FestivalSearchViewHolder(
    private val binding: ItemSearchFestivalBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val artistAdapter = ArtistAdapter()

    init {
        binding.rvFestivalArtists.adapter = artistAdapter
        binding.rvFestivalArtists.addItemDecoration(ArtistItemDecoration())
    }

    fun bind(item: FestivalSearchItemUiState) {
        binding.item = item
        artistAdapter.submitList(item.artists)
        binding.tvFestivalDDay.bindFestivalDday(item)
    }

    private fun TextView.bindFestivalDday(item: FestivalSearchItemUiState) {
        when {
            LocalDate.now() > item.endDate -> {
                binding.tvFestivalDDay.visibility = View.GONE
                binding.tvFestivalDDayEnd.visibility = View.VISIBLE
            }

            LocalDate.now() >= item.startDate -> {
                text = context.getString(R.string.festival_list_tv_dday_in_progress)
                setTextColor(context.getColor(R.color.secondary_pink_01))
                background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_festival_list_dday_in_progress,
                )
                binding.tvFestivalDDay.visibility = View.VISIBLE
                binding.tvFestivalDDayEnd.visibility = View.GONE
            }

            else -> {
                val dDay = LocalDate.now().toEpochDay() - item.startDate.toEpochDay()
                val backgroundColor = if (dDay >= -7L) {
                    context.getColor(R.color.secondary_pink_01)
                } else {
                    context.getColor(R.color.contents_gray_07)
                }
                setBackgroundColor(backgroundColor)
                setTextColor(context.getColor(R.color.background_gray_01))
                text = context.getString(R.string.tv_dday_format, dDay.toString())
                binding.tvFestivalDDay.visibility = View.VISIBLE
                binding.tvFestivalDDayEnd.visibility = View.GONE
            }
        }
    }

    private class ArtistItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = 8.dpToPx
        }

        private val Int.dpToPx: Int
            get() = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                Resources.getSystem().displayMetrics,
            ).toInt()
    }

    companion object {
        fun of(parent: ViewGroup): FestivalSearchViewHolder {
            val binding = ItemSearchFestivalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalSearchViewHolder(binding)
        }
    }
}
