package com.festago.festago.presentation.ui.schooldetail

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
import com.festago.festago.presentation.ui.schooldetail.uistate.FestivalItemUiState
import java.time.LocalDate

class SchoolDetailFestivalViewHolder(
    private val binding: ItemSchoolDetailFestivalBinding,
) : SchoolDetailViewHolder(binding) {
    private val artistAdapter = ArtistAdapter()

    init {
        binding.rvFestivalArtists.adapter = artistAdapter
        binding.rvFestivalArtists.addItemDecoration(ArtistItemDecoration())
        binding.rvFestivalArtists.itemAnimator = null
    }

    fun bind(item: FestivalItemUiState) {
        binding.item = item
        artistAdapter.submitList(item.artists)
        bindDDayView(item)
    }

    private fun bindDDayView(item: FestivalItemUiState) {
        val context = binding.root.context

        when {
            LocalDate.now() in item.startDate..item.endDate -> {
                binding.tvFestivalDDay.text =
                    context.getString(R.string.tv_dday_in_progress)
                binding.tvFestivalDDay.setTextColor(context.getColor(R.color.secondary_pink_01))
                binding.tvFestivalDDay.background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_festival_list_dday_in_progress,
                )
                binding.tvFestivalDDay.visibility = View.VISIBLE
                binding.tvFestivalDDayEnd.visibility = View.GONE
            }

            LocalDate.now() < item.startDate -> {
                val dDay = LocalDate.now().toEpochDay() - item.startDate.toEpochDay()
                val backgroundColor = if (dDay >= -7L) {
                    context.getColor(R.color.secondary_pink_01)
                } else {
                    context.getColor(R.color.contents_gray_07)
                }
                binding.tvFestivalDDay.setBackgroundColor(backgroundColor)
                binding.tvFestivalDDay.setTextColor(context.getColor(R.color.background_gray_01))
                binding.tvFestivalDDay.text =
                    context.getString(R.string.tv_dday_format, dDay.toString())
                binding.tvFestivalDDay.visibility = View.VISIBLE
                binding.tvFestivalDDayEnd.visibility = View.GONE
            }

            else -> {
                binding.tvFestivalDDay.visibility = View.GONE
                binding.tvFestivalDDayEnd.visibility = View.VISIBLE
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
