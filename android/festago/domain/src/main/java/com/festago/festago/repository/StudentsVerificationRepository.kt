package com.festago.festago.repository

interface StudentsVerificationRepository {
    suspend fun sendVerificationCode(userName: String, schoolId: Int): Result<Unit>
}
