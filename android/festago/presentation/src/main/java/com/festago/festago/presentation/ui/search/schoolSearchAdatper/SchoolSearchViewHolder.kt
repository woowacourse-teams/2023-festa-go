package com.festago.festago.presentation.ui.search.schoolSearchAdatper

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemSearchSchoolBinding
import com.festago.festago.presentation.ui.search.uistate.SchoolSearchItemUiState
import java.time.LocalDate

class SchoolSearchViewHolder(
    private val binding: ItemSearchSchoolBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SchoolSearchItemUiState) {
        binding.item = item
        binding.tvSchoolFestivalDday.setSchoolFestivalDday(item.upcomingFestivalStartDate)
    }

    private fun TextView.setSchoolFestivalDday(
        upcomingFestivalStartDate: LocalDate?,
    ) {
        when {
            upcomingFestivalStartDate == null -> {
                text = context.getString(R.string.search_school_tv_no_plan)
                setTextColor(context.getColor(R.color.contents_gray_05))
            }

            LocalDate.now() >= upcomingFestivalStartDate -> {
                text = context.getString(R.string.search_school_tv_dday_in_progress)
                setTextColor(context.getColor(R.color.secondary_pink_01))
            }

            LocalDate.now() < upcomingFestivalStartDate -> {
                val dDay =
                    LocalDate.now().toEpochDay() - upcomingFestivalStartDate.toEpochDay()
                text = context.getString(R.string.search_school_tv_dday_format, dDay.toString())
                val colorId =
                    if (dDay >= -7L) R.color.secondary_pink_01 else R.color.contents_gray_07
                setTextColor(context.getColor(colorId))
            }
        }
    }

    companion object {
        fun of(parent: ViewGroup): SchoolSearchViewHolder {
            val binding = ItemSearchSchoolBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return SchoolSearchViewHolder(binding)
        }
    }
}
