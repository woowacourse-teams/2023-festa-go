package com.festago.festago.data.repository

import com.festago.festago.domain.model.festival.FestivalsPage
import com.festago.festago.domain.model.school.SchoolInfo
import com.festago.festago.domain.repository.SchoolRepository
import javax.inject.Inject

class SchoolDefaultRepository @Inject constructor(
    // TODO: Service 연결
) : SchoolRepository {
    override suspend fun loadSchoolInfo(schoolId: Long): Result<SchoolInfo> {
        // TODO: API 연동
        return Result.success(FakeSchool.googleSchool)
    }

    override suspend fun loadSchoolFestivals(schoolId: Long): Result<FestivalsPage> {
        // TODO: API 연동
        return Result.success(
            FestivalsPage(
                isLastPage = true,
                festivals = FakeFestivals.progressFestivals,
            )
        )
    }
}
