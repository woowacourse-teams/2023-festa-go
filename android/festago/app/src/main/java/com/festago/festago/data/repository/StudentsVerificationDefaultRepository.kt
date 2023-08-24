package com.festago.festago.data.repository

import com.festago.festago.data.service.StudentsVerificationRetrofitService
import com.festago.festago.repository.StudentsVerificationRepository

class StudentsVerificationDefaultRepository(
    private val studentsVerificationRetrofitService: StudentsVerificationRetrofitService,
) : StudentsVerificationRepository {

    override suspend fun sendVerificationCode(userName: String, schoolId: Int): Result<Unit> {
        // TODO: API 연동 작업 필요
        return Result.success(Unit)

//        studentsVerificationRetrofitService.sendVerificationCode(
//            SendVerificationRequest(userName = userName, schoolId = schoolId),
//        ).runCatchingWithErrorHandler()
//            .getOrElse { error -> return Result.failure(error) }
//            .let { return Result.success(Unit) }
    }
}
