package com.festago.festago.data.dto.schooldetail

import com.festago.festago.data.dto.school.SocialMediaResponse
import com.festago.festago.domain.model.school.SchoolInfo
import kotlinx.serialization.Serializable

@Serializable
data class SchoolInfoResponse(
    val id: Int,
    val name: String?,
    val logoUrl: String?,
    val backgroundImageUrl: String?,
    val socialMedias: List<SocialMediaResponse>,
) {
    fun toDomain(): SchoolInfo = SchoolInfo(
        id = id,
        schoolName = name ?: "",
        logoUrl = logoUrl ?: "",
        backgroundUrl = backgroundImageUrl ?: "",
        socialMedia = socialMedias.map { it.toDomain() },
    )
}
