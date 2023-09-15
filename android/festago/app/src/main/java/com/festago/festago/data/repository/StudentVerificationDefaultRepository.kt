package com.festago.festago.data.repository

import com.festago.festago.data.service.StudentVerificationRetrofitService
import com.festago.festago.model.StudentVerificationCode
import com.festago.festago.repository.StudentVerificationRepository

class StudentVerificationDefaultRepository(
    private val studentVerificationRetrofitService: StudentVerificationRetrofitService,
) : StudentVerificationRepository {

    override suspend fun sendVerificationCode(userName: String, schoolId: Long): Result<Unit> {
        // TODO: API 연동 작업 필요
        return Result.success(Unit)
    }

    override suspend fun requestVerificationCodeConfirm(code: StudentVerificationCode): Result<Unit> {
        // TODO: API 연동 작업 필요
        return Result.success(Unit)
    }
}
