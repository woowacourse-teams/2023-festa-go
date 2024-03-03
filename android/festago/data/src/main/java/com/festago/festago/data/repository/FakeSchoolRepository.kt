package com.festago.festago.data.repository

import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.school.SchoolInfo
import com.festago.festago.domain.repository.SchoolRepository
import javax.inject.Inject

class FakeSchoolRepository @Inject constructor() : SchoolRepository {
    override suspend fun loadSchoolInfo(schoolId: Long): Result<SchoolInfo> {
        return Result.success(FakeSchool.googleSchool)
    }

    override suspend fun loadSchoolFestivals(schoolId: Long): Result<FestivalsPage> {
        return Result.success(
            FestivalsPage(
                isLastPage = true,
                festivals = FakeFestivals.progressFestivals,
            )
        )
    }
}
