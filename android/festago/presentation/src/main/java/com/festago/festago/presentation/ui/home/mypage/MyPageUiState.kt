package com.festago.festago.presentation.ui.home.mypage

sealed interface MyPageUiState {
    object Loading : MyPageUiState

    object Success : MyPageUiState

    class Error(
        val refresh: () -> Unit,
    ) : MyPageUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
