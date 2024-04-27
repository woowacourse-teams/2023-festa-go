package com.festago.festago.presentation.ui.schooldetail.uistate

import com.festago.festago.domain.model.school.SchoolInfo

sealed interface SchoolDetailUiState {
    object Loading : SchoolDetailUiState

    data class Success(
        val schoolInfo: SchoolInfo,
        val festivals: List<FestivalItemUiState>,
        val isLast: Boolean,
    ) : SchoolDetailUiState

    class Error(val refresh: (schoolId: Long) -> Unit) : SchoolDetailUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
    val shouldShowEmptyFestivals get() = this is Success && festivals.isEmpty()
}
