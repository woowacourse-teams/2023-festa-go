package com.festago.festago.domain.repository

import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.school.SchoolInfo
import java.time.LocalDate

interface SchoolRepository {
    suspend fun loadSchoolInfo(schoolId: Long): Result<SchoolInfo>
    suspend fun loadSchoolFestivals(
        schoolId: Long,
        size: Int? = null,
        isPast: Boolean? = null,
        lastFestivalId: Int? = null,
        lastStartDate: LocalDate? = null,
    ): Result<FestivalsPage>
}
