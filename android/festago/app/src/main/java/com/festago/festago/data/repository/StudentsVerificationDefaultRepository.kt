package com.festago.festago.data.repository

import com.festago.festago.data.service.StudentsVerificationRetrofitService
import com.festago.festago.model.StudentVerificationCode
import com.festago.festago.repository.StudentsVerificationRepository

class StudentsVerificationDefaultRepository(
    private val studentsVerificationRetrofitService: StudentsVerificationRetrofitService,
) : StudentsVerificationRepository {

    override suspend fun sendVerificationCode(userName: String, schoolId: Long): Result<Unit> {
        // TODO: API 연동 작업 필요
        return Result.success(Unit)
    }

    override suspend fun requestVerificationCodeConfirm(code: StudentVerificationCode): Result<Unit> {
        // TODO: API 연동 작업 필요
        return Result.success(Unit)
    }
}
