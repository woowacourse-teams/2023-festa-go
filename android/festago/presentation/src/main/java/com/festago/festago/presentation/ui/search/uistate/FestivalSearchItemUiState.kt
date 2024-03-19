package com.festago.festago.presentation.ui.search.uistate

import java.time.LocalDate

data class FestivalSearchItemUiState(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String,
    val schoolUiState: SchoolUiState,
    val artists: List<ArtistUiState>,
    val onFestivalSearchClick: (festivalId: Long) -> Unit,
)
