package com.festago.festago.presentation.ui.home.festivallist

import com.festago.festago.presentation.model.FestivalUiModel

sealed interface FestivalListUiState {
    object Loading : FestivalListUiState

    data class Success(val festivals: List<FestivalUiModel>) : FestivalListUiState

    object Error : FestivalListUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
