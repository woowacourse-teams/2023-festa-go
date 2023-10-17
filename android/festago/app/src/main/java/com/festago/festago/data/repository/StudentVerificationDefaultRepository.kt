package com.festago.festago.data.repository

import com.festago.festago.data.dto.SendVerificationRequest
import com.festago.festago.data.dto.VerificationRequest
import com.festago.festago.data.service.StudentVerificationRetrofitService
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.model.StudentVerificationCode
import com.festago.festago.repository.StudentVerificationRepository
import javax.inject.Inject

class StudentVerificationDefaultRepository @Inject constructor(
    private val studentVerificationRetrofitService: StudentVerificationRetrofitService,
) : StudentVerificationRepository {

    override suspend fun sendVerificationCode(userName: String, schoolId: Long): Result<Unit> =
        runCatchingResponse {
            studentVerificationRetrofitService.sendVerificationCode(
                SendVerificationRequest(userName, schoolId),
            )
        }

    override suspend fun requestVerificationCodeConfirm(code: StudentVerificationCode): Result<Unit> =
        runCatchingResponse {
            studentVerificationRetrofitService.requestVerification(
                VerificationRequest.from(code),
            )
        }
}
