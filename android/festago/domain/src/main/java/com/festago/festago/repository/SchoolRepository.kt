package com.festago.festago.repository

interface SchoolRepository {
    suspend fun loadSchoolEmail(schoolId: Long): Result<String>
}
