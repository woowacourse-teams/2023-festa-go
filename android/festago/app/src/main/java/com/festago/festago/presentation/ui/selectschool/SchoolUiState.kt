package com.festago.festago.presentation.ui.selectschool

data class SchoolUiState(
    val id: Long,
    val domain: String,
    val name: String,
    val selectedSchool: (schoolId: Long) -> Unit,
)
