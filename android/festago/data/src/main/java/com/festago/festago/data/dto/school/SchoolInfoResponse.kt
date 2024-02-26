package com.festago.festago.data.dto.school

import com.festago.festago.domain.model.school.SchoolInfo
import kotlinx.serialization.Serializable

@Serializable
data class SchoolInfoResponse(
    val id: Int,
    val schoolName: String,
    val logoUrl: String,
    val backgroundUrl: String,
    val socialMediaResponse: List<SocialMediaResponse>,
) {
    fun toDomain(): SchoolInfo = SchoolInfo(
        id = id,
        schoolName = schoolName,
        logoUrl = logoUrl,
        backgroundUrl = backgroundUrl,
        socialMedia = socialMediaResponse.map { it.toDomain() },
    )
}
