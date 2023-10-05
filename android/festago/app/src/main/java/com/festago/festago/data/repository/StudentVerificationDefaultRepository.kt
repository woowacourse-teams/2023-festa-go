package com.festago.festago.data.repository

import com.festago.festago.data.dto.SendVerificationRequest
import com.festago.festago.data.dto.VerificationRequest
import com.festago.festago.data.service.StudentVerificationRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.model.StudentVerificationCode
import com.festago.festago.repository.StudentVerificationRepository
import javax.inject.Inject

class StudentVerificationDefaultRepository @Inject constructor(
    private val studentVerificationRetrofitService: StudentVerificationRetrofitService,
) : StudentVerificationRepository {

    override suspend fun sendVerificationCode(userName: String, schoolId: Long): Result<Unit> {
        studentVerificationRetrofitService.sendVerificationCode(
            SendVerificationRequest(userName, schoolId),
        ).runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(Unit) }
    }

    override suspend fun requestVerificationCodeConfirm(code: StudentVerificationCode): Result<Unit> {
        studentVerificationRetrofitService.requestVerification(
            VerificationRequest.from(code),
        ).runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(Unit) }
    }
}
