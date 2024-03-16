package com.festago.festago.presentation.ui.home.festivallist.uistate

data class FestivalTabUiState(
    val selectedFilter: FestivalFilterUiState,
    val onFilterSelected: (FestivalFilterUiState) -> Unit,
)
