package com.festago.festago.presentation.ui.festivaldetail.uiState

interface FestivalDetailUiState {
    object Loading : FestivalDetailUiState

    data class Success(
        val festival: FestivalUiState,
        val bookmarked: Boolean,
        val stages: List<StageItemUiState>,
        val onBookmarkClick: (Long) -> Unit,
    ) : FestivalDetailUiState

    object Error : FestivalDetailUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowEmptyStages get() = shouldShowSuccess && (this as Success).stages.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
