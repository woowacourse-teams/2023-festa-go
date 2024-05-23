package com.festago.festago.data.repository

import com.festago.festago.data.service.SchoolRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.school.SchoolInfo
import com.festago.festago.domain.repository.SchoolRepository
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject

class DefaultSchoolRepository @Inject constructor(
    private val schoolRetrofitService: SchoolRetrofitService,
) : SchoolRepository {
    override suspend fun loadSchoolInfo(schoolId: Long, delayTimeMillis: Long): Result<SchoolInfo> {
        delay(delayTimeMillis)
        return runCatchingResponse {
            schoolRetrofitService.getSchool(schoolId)
        }.onSuccessOrCatch { it.toDomain() }
    }

    override suspend fun loadSchoolFestivals(
        schoolId: Long,
        size: Int?,
        isPast: Boolean?,
        lastFestivalId: Int?,
        lastStartDate: LocalDate?,
    ): Result<FestivalsPage> {
        return runCatchingResponse {
            schoolRetrofitService.getSchoolFestivals(
                schoolId = schoolId,
                size = size,
                isPast = isPast,
                lastFestivalId = lastFestivalId,
                lastStartDate = lastStartDate,
            )
        }.onSuccessOrCatch { it.toDomain() }
    }
}
