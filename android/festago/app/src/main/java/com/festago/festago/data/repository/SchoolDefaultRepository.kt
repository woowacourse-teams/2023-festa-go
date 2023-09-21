package com.festago.festago.data.repository

import com.festago.festago.data.service.SchoolRetrofitService
import com.festago.festago.model.School
import com.festago.festago.repository.SchoolRepository
import javax.inject.Inject

class SchoolDefaultRepository @Inject constructor(
    private val schoolRetrofitService: SchoolRetrofitService
) : SchoolRepository {

    override suspend fun loadSchools(): Result<List<School>> {
        // TODO: API 연동 작업 필요
        return Result.success(
            listOf(
                School(1, "hash.ac.kr", "해시대학교"),
                School(2, "pooh.ac.kr", "푸우대학교"),
                School(3, "ash.ac.kr", "애쉬대학교"),
            )
        )
    }

    override suspend fun loadSchoolEmail(schoolId: Long): Result<String> {
        // TODO: API 연동 작업 필요
        return Result.success("festago.com")
    }
}
