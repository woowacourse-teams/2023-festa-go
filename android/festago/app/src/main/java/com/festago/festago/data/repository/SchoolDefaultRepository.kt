package com.festago.festago.data.repository

import com.festago.festago.data.service.SchoolRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.model.School
import com.festago.festago.repository.SchoolRepository
import javax.inject.Inject

class SchoolDefaultRepository @Inject constructor(
    private val schoolRetrofitService: SchoolRetrofitService
) : SchoolRepository {

    override suspend fun loadSchools(): Result<List<School>> {
        schoolRetrofitService.getSchools()
            .runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }

    override suspend fun loadSchoolEmail(schoolId: Long): Result<String> {
        // TODO: API 연동 작업 필요
        return Result.success("festago.com")
    }
}
