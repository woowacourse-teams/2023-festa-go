package com.festago.festago.data.repository

import com.festago.festago.data.service.SchoolRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.model.School
import com.festago.festago.repository.SchoolRepository
import javax.inject.Inject

class SchoolDefaultRepository @Inject constructor(
    private val schoolRetrofitService: SchoolRetrofitService,
) : SchoolRepository {

    override suspend fun loadSchools(): Result<List<School>> =
        runCatchingResponse { schoolRetrofitService.getSchools() }
            .onSuccessOrCatch { it.toDomain() }

    override suspend fun loadSchoolEmail(schoolId: Long): Result<String> {
        return runCatchingResponse { schoolRetrofitService.getSchools() }
            .onSuccessOrCatch {
                val school = it.schools.find { school -> school.id.toLong() == schoolId }
                school?.domain ?: throw IllegalArgumentException(MATCH_SCHOOL_NOT_FOUND)
            }
    }

    companion object {
        private const val MATCH_SCHOOL_NOT_FOUND = "MATCH_SCHOOL_NOT_FOUND"
    }
}
