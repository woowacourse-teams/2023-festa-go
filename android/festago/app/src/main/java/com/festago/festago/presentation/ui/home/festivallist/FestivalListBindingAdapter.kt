package com.festago.festago.presentation.ui.home.festivallist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.festago.festago.R
import com.festago.festago.presentation.model.FestivalUiModel
import java.time.format.DateTimeFormatter

@BindingAdapter("dateRange")
fun TextView.setDateRange(festival: FestivalUiModel) {
    val datePattern = context.getString(R.string.festival_list_tv_date_format)

    this.text = context.getString(R.string.festival_list_tv_date_range_format).format(
        festival.startDate.format(DateTimeFormatter.ofPattern(datePattern)),
        festival.endDate.format(DateTimeFormatter.ofPattern(datePattern)),
    )
}
