package com.festago.festago.data.dto.schooldetail

import com.festago.festago.data.dto.school.SocialMediaResponse
import com.festago.festago.domain.model.school.SchoolInfo
import kotlinx.serialization.Serializable

@Serializable
data class SchoolInfoResponse(
    val id: Int,
    val schoolName: String?,
    val logoUrl: String?,
    val backgroundUrl: String?,
    val socialMedias: List<SocialMediaResponse>,
) {
    fun toDomain(): SchoolInfo = SchoolInfo(
        id = id,
        schoolName = schoolName ?: "",
        logoUrl = logoUrl ?: "",
        backgroundUrl = backgroundUrl ?: "",
        socialMedia = socialMedias.map { it.toDomain() },
    )
}
