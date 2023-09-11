package com.festago.festago.presentation.ui.studentverification

sealed interface StudentVerificationUiState {
    object Loading : StudentVerificationUiState

    data class Success(
        val schoolEmail: String,
        val remainTime: Int,
        val isValidateCode: Boolean = false,
    ) : StudentVerificationUiState

    object Error : StudentVerificationUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
