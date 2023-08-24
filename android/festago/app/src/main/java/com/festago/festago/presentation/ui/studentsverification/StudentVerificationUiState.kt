package com.festago.festago.presentation.ui.studentsverification

sealed interface StudentVerificationUiState {
    object Loading : StudentVerificationUiState

    data class Success(val schoolEmail: String, val remainTime: Int) : StudentVerificationUiState

    object Error : StudentVerificationUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
