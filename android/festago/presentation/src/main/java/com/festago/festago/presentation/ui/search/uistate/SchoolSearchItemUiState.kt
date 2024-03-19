package com.festago.festago.presentation.ui.search.uistate

import com.festago.festago.presentation.ui.home.festivallist.uistate.ArtistUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.SchoolUiState
import java.time.LocalDate

data class SchoolSearchItemUiState(
    val id: Long,
    val name: String,
    val logoUrl: String,
    val upcomingFestivalStartDate: LocalDate?,
    val onSchoolSearchClick: (schoolId: Long) -> Unit,
)

