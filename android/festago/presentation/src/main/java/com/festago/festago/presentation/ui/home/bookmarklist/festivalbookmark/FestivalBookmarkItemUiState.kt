package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import java.time.LocalDate

data class FestivalBookmarkItemUiState(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val imageUrl: String,
    val artists: List<ArtistUiState>,
    val onFestivalDetail: (festivalId: Long) -> Unit,
)
