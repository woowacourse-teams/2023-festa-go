package com.festago.festago.presentation.ui.home.festivallist.uistate

import com.festago.festago.domain.model.festival.SchoolRegion

data class FestivalTabUiState(
    val selectedFilter: FestivalFilterUiState,
    val selectedRegion: SchoolRegion?,
    val onFilterSelected: (FestivalFilterUiState) -> Unit,
    val onRegionClick: () -> Unit,
)
