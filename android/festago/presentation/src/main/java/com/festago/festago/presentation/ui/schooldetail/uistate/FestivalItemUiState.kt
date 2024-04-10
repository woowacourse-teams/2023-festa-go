package com.festago.festago.presentation.ui.schooldetail.uistate

import java.time.LocalDate

data class FestivalItemUiState(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String,
    val artists: List<ArtistUiState>,
    val onFestivalDetailClick: (Long) -> Unit,
)
