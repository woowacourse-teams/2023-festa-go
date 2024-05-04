package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.adapater

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemFestivalBookmarkBinding
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.artistadapter.ArtistAdapter
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.uistate.FestivalBookmarkItemUiState
import java.time.LocalDate

class FestivalBookmarkViewHolder(
    val binding: ItemFestivalBookmarkBinding,
) :
    RecyclerView.ViewHolder(binding.root) {
    private val artistAdapter = ArtistAdapter()

    init {
        binding.rvFestivalArtists.adapter = artistAdapter
        binding.rvFestivalArtists.addItemDecoration(ArtistItemDecoration())
    }

    fun bind(item: FestivalBookmarkItemUiState) {
        binding.item = item
        artistAdapter.submitList(item.artists)
        bindDDayView(item)
    }

    private fun bindDDayView(item: FestivalBookmarkItemUiState) {
        val context = binding.root.context

        val dDayView = binding.tvFestivalDDay
        when {
            LocalDate.now() > item.endDate -> {
                binding.tvFestivalDDay.visibility = View.GONE
                binding.tvFestivalDDayEnd.visibility = View.VISIBLE
            }

            LocalDate.now() >= item.startDate -> {
                dDayView.text = context.getString(R.string.festival_list_tv_dday_in_progress)
                dDayView.setTextColor(context.getColor(R.color.secondary_pink_01))
                dDayView.background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_festival_list_dday_in_progress,
                )
                binding.tvFestivalDDay.visibility = View.VISIBLE
                binding.tvFestivalDDayEnd.visibility = View.GONE
            }

            LocalDate.now() >= item.startDate.minusDays(7) -> {
                binding.tvFestivalDDay.visibility = View.VISIBLE
                binding.tvFestivalDDayEnd.visibility = View.GONE
                dDayView.setTextColor(context.getColor(R.color.background_gray_01))
                dDayView.text = context.getString(
                    R.string.festival_list_tv_dday_format,
                    LocalDate.now().compareTo(item.startDate).toString(),
                )
                dDayView.setBackgroundColor(0xffff1273.toInt())
            }

            else -> binding.tvFestivalDDay.apply {
                binding.tvFestivalDDay.visibility = View.VISIBLE
                binding.tvFestivalDDayEnd.visibility = View.GONE
                dDayView.setTextColor(context.getColor(R.color.background_gray_01))
                dDayView.text = context.getString(
                    R.string.festival_list_tv_dday_format,
                    (LocalDate.now().toEpochDay() - item.startDate.toEpochDay()).toString(),
                )
                dDayView.setBackgroundColor(context.getColor(android.R.color.black))
            }
        }
    }

    private class ArtistItemDecoration : ItemDecoration() {
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
        fun of(parent: ViewGroup): FestivalBookmarkViewHolder {
            val binding = ItemFestivalBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalBookmarkViewHolder(binding)
        }
    }
}
