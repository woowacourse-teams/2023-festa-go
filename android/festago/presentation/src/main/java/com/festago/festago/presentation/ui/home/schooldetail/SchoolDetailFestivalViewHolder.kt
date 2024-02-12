package com.festago.festago.presentation.ui.home.schooldetail

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
import com.festago.festago.presentation.databinding.ItemSchoolDetailFestivalBinding
import com.festago.festago.presentation.ui.home.schooldetail.uistate.FestivalItemUiState
import java.time.LocalDate

class SchoolDetailFestivalViewHolder(
    private val binding: ItemSchoolDetailFestivalBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val artistAdapter = ArtistAdapter()

    init {
        binding.rvFestivalArtists.adapter = artistAdapter
        binding.rvFestivalArtists.addItemDecoration(ArtistItemDecoration())
    }

    fun bind(item: FestivalItemUiState) {
        binding.item = item
        artistAdapter.submitList(item.artists)
        bindDDayView(item)
    }

    private fun bindDDayView(item: FestivalItemUiState) {
        val context = binding.root.context

        val dDayView = binding.tvFestivalDDay
        when {
            LocalDate.now() > item.endDate -> Unit

            LocalDate.now() >= item.startDate -> {
                dDayView.text = context.getString(R.string.festival_list_tv_dday_in_progress)
                dDayView.setTextColor(context.getColor(R.color.secondary_pink_01))
                dDayView.background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_festival_list_dday_in_progress,
                )
            }

            LocalDate.now() == item.startDate.minusDays(1) -> {
                dDayView.text = context.getString(
                    R.string.festival_list_tv_dday_format,
                    item.startDate.compareTo(LocalDate.now()).toString(),
                )
                dDayView.setBackgroundColor(0xffff1273.toInt())
            }

            else -> binding.tvFestivalDDay.apply {
                dDayView.text = context.getString(
                    R.string.festival_list_tv_dday_format,
                    item.startDate.compareTo(LocalDate.now()).toString(),
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
        fun of(parent: ViewGroup): SchoolDetailFestivalViewHolder {
            val binding = ItemSchoolDetailFestivalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return SchoolDetailFestivalViewHolder(binding)
        }
    }
}
