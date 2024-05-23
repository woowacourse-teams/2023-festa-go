package com.festago.festago.presentation.ui.search.uistate

import java.time.LocalDate

data class SchoolSearchItemUiState(
    val id: Long,
    val name: String,
    val logoUrl: String,
    val upcomingFestivalStartDate: LocalDate?,
    val onSchoolSearchClick: (school: SchoolSearchItemUiState) -> Unit,
)
