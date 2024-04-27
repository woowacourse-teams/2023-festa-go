package com.festago.festago.presentation.ui.festivaldetail.uiState

import com.festago.festago.domain.model.school.School
import com.festago.festago.domain.model.social.SocialMedia
import java.time.LocalDate

data class FestivalUiState(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val posterImageUrl: String,
    val school: School,
    val socialMedias: List<SocialMedia>,
    val onSchoolClick: (Long) -> Unit,
)
