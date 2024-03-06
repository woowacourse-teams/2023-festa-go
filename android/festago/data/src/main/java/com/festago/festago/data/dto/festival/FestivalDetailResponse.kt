package com.festago.festago.data.dto.festival

import com.festago.festago.data.dto.school.SchoolResponse
import com.festago.festago.data.dto.school.SocialMediaResponse
import com.festago.festago.data.dto.stage.StageResponse
import com.festago.festago.domain.model.festival.FestivalDetail
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class FestivalDetailResponse(
    val id: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val posterImageUrl: String,
    val school: SchoolResponse,
    val socialMedias: List<SocialMediaResponse>,
    val stages: List<StageResponse>,
) {
    fun toDomain() = FestivalDetail(
        id,
        name,
        LocalDate.parse(startDate),
        LocalDate.parse(endDate),
        posterImageUrl,
        school.toDomain(),
        socialMedias.map { it.toDomain() },
        stages.map { it.toDomain() },
    )
}
