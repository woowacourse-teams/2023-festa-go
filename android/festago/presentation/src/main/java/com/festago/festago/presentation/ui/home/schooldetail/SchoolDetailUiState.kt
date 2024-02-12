package com.festago.festago.presentation.ui.home.schooldetail

import com.festago.festago.domain.model.school.SchoolInfo
import com.festago.festago.presentation.ui.home.schooldetail.uistate.FestivalItemUiState

sealed interface SchoolDetailUiState {
    object Loading : SchoolDetailUiState

    data class Success(
        val schoolInfo: SchoolInfo,
        val festivals: List<FestivalItemUiState>,
        val isLast: Boolean,
    ) : SchoolDetailUiState

    object Error : SchoolDetailUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
