package com.festago.festago.data.repository

import com.festago.festago.repository.SchoolRepository

class SchoolDefaultRepository() : SchoolRepository {

    override suspend fun loadSchoolEmail(schoolId: Int): Result<String> {
        // TODO: API 연동 작업 필요
        return Result.success("festago.com")
    }
}
