package com.festago.festago.repository

import com.festago.festago.model.StudentVerificationCode

interface StudentsVerificationRepository {
    suspend fun sendVerificationCode(userName: String, schoolId: Long): Result<Unit>
    suspend fun requestVerificationCodeConfirm(code: StudentVerificationCode): Result<Unit>
}
