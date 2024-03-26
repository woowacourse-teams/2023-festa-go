package com.festago.festago.presentation.ui.home.festivallist.uistate

import com.festago.festago.domain.model.festival.SchoolRegion

sealed interface FestivalListUiState {
    object Loading : FestivalListUiState

    data class Success(
        val popularFestivalUiState: PopularFestivalUiState,
        val festivals: List<FestivalItemUiState>,
        val festivalFilter: FestivalFilterUiState,
        val isLastPage: Boolean,
        val schoolRegion: SchoolRegion? = null,
    ) : FestivalListUiState

    object Error : FestivalListUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
