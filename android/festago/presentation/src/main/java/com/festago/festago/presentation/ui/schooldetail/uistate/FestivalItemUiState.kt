package com.festago.festago.presentation.ui.schooldetail.uistate

import java.time.LocalDate

data class FestivalItemUiState(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String,
    val schoolUiState: SchoolUiState,
    val artists: List<ArtistUiState>,
)
