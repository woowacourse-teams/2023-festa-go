package com.festago.festago.presentation.ui.home.schooldetail

import com.festago.festago.domain.model.festival.Festival
import com.festago.festago.domain.model.school.SchoolInfo

sealed interface SchoolDetailUiState {
    object Loading : SchoolDetailUiState

    data class Success(
        val schoolInfo: SchoolInfo,
        val festivals: List<Festival>,
        val isLast: Boolean,
    ) : SchoolDetailUiState

    object Error : SchoolDetailUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
