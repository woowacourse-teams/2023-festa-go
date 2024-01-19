package com.festago.festago.presentation.ui.home.festivallist.uistate

sealed interface FestivalListUiState {
    object Loading : FestivalListUiState

    data class Success(
        val popularFestivals: List<FestivalItemUiState>,
        val festivals: List<FestivalItemUiState>,
    ) : FestivalListUiState

    object Error : FestivalListUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
