package com.festago.festago.domain.model.festival

import com.festago.festago.domain.model.school.School
import com.festago.festago.domain.model.social.SocialMedia
import com.festago.festago.domain.model.stage.Stage
import java.time.LocalDate

data class FestivalDetail(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val posterImageUrl: String,
    val school: School,
    val socialMedias: List<SocialMedia>,
    val stages: List<Stage>,
)
