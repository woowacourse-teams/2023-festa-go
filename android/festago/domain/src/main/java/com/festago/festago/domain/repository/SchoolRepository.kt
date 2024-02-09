package com.festago.festago.domain.repository

import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.school.SchoolInfo

interface SchoolRepository {
    suspend fun loadSchoolInfo(): Result<SchoolInfo>
    suspend fun loadSchoolFestivals(): Result<FestivalsPage>
}
