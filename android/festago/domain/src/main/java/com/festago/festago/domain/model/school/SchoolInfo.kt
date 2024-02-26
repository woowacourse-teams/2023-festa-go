package com.festago.festago.domain.model.school

import com.festago.festago.domain.model.social.SocialMedia

data class SchoolInfo(
    val id: Int,
    val schoolName: String,
    val logoUrl: String,
    val backgroundUrl: String,
    val socialMedia: List<SocialMedia>
)
