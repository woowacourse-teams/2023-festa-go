package com.festago.festago.presentation.ui.home.festivallist

import com.festago.festago.presentation.model.FestivalItemUiState

sealed interface FestivalListUiState {
    object Loading : FestivalListUiState

    data class Success(
        val festivals: List<FestivalItemUiState>
    ) : FestivalListUiState {
        val hasFestival get() = festivals.isNotEmpty()
    }

    object Error : FestivalListUiState

    val shouldShowSuccess get() = this is Success && hasFestival
    val shouldShowSuccessAndEmpty get() = this is Success && !hasFestival
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
