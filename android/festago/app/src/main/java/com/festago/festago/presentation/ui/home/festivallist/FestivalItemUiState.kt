package com.festago.festago.presentation.ui.home.festivallist

import java.time.LocalDate

data class FestivalItemUiState(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val thumbnail: String,
    val onFestivalDetail: (festivalId: Long) -> Unit,
)
