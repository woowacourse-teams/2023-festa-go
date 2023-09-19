package com.festago.festago.data.repository

import com.festago.festago.repository.SchoolRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolDefaultRepository @Inject constructor() : SchoolRepository {

    override suspend fun loadSchoolEmail(schoolId: Long): Result<String> {
        // TODO: API 연동 작업 필요
        return Result.success("festago.com")
    }
}
