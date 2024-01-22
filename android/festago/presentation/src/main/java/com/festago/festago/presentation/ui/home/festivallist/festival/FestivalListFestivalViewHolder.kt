package com.festago.festago.presentation.ui.home.festivallist.festival

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemFestivalListFestivalBinding
import com.festago.festago.presentation.ui.home.festivallist.festival.artistlist.ArtistAdapter
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import java.time.LocalDate

class FestivalListFestivalViewHolder(private val binding: ItemFestivalListFestivalBinding) :
    FestivalListViewHolder(binding) {
    private val artistAdapter = ArtistAdapter()

    init {
        binding.rvFestivalArtists.adapter = artistAdapter
        binding.rvFestivalArtists.addItemDecoration(ArtistItemDecoration(binding.root.context))
    }

    fun bind(item: FestivalItemUiState) {
        binding.item = item
        artistAdapter.submitList(item.artists)
        bindDDayView(item)
    }

    private fun bindDDayView(item: FestivalItemUiState) {
        val context = binding.root.context
        when {
            LocalDate.now() > item.endDate -> {
                binding.tvFestivalDDay.text = context.getString(R.string.festival_list_tv_dday_end)
                binding.tvFestivalDDay.setBackgroundColor(context.getColor(android.R.color.darker_gray))
            }

            LocalDate.now() >= item.startDate -> {
                binding.tvFestivalDDay.text =
                    context.getString(R.string.festival_list_tv_dday_in_progress)
                binding.tvFestivalDDay.setBackgroundColor(0xffff1273.toInt())
            }

            else -> {
                binding.tvFestivalDDay.text = context.getString(
                    R.string.festival_list_tv_dday_format,
                    item.startDate.compareTo(LocalDate.now()).toString(),
                )
                binding.tvFestivalDDay.setBackgroundColor(context.getColor(android.R.color.black))
            }
        }
    }

    private class ArtistItemDecoration(val context: Context) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = 8.dpToPx(context)
        }

        private fun Int.dpToPx(context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this.toFloat(),
                context.resources.displayMetrics,
            ).toInt()
        }
    }

    companion object {
        fun of(parent: ViewGroup): FestivalListFestivalViewHolder {
            val binding = ItemFestivalListFestivalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return FestivalListFestivalViewHolder(binding)
        }
    }
}
