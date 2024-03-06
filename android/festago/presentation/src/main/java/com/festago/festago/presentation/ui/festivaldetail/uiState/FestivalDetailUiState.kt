package com.festago.festago.presentation.ui.festivaldetail.uiState

interface FestivalDetailUiState {
    object Loading : FestivalDetailUiState

    data class Success(
        val festival: FestivalUiState,
        val stages: List<StageItemUiState>,
    ) : FestivalDetailUiState

    object Error : FestivalDetailUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
