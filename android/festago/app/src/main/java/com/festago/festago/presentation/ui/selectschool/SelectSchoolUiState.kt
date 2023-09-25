package com.festago.festago.presentation.ui.selectschool

import com.festago.festago.model.School

interface SelectSchoolUiState {
    object Loading : SelectSchoolUiState

    data class Success(
        val schools: List<School>,
        val selectedSchoolId: Long? = null
    ) : SelectSchoolUiState {
        val schoolSelected = selectedSchoolId != null
    }

    object Error : SelectSchoolUiState

    val enableNext get() = (this is Success) && schoolSelected
    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
