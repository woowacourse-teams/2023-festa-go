package com.festago.festago.repository

import com.festago.festago.model.School

interface SchoolRepository {
    suspend fun loadSchools(): Result<List<School>>
    suspend fun loadSchoolEmail(schoolId: Long): Result<String>
}
