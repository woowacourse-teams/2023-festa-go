package com.festago.festago.data.repository

import com.festago.festago.domain.model.school.SchoolInfo
import com.festago.festago.domain.model.social.SocialMedia

object FakeSchool {
    val googleSchool = SchoolInfo(
        id = 1,
        schoolName = "구글대학교",
        logoUrl = "https://cdn1.iconfinder.com/data/icons/logos-brands-in-colors/544/Google__G__Logo-512.png",
        backgroundUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/1200px-Google_2015_logo.svg.png",
        socialMedia = listOf(
            SocialMedia(
                type = "INSTAGRAM",
                name = "구글대학교 인스타",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Instagram_logo_2016.svg/2048px-Instagram_logo_2016.svg.png",
                url = "https://www.instagram.com/",
            )
        )
    )
}
