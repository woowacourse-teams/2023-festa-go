package com.festago.festago.data.dto

import com.festago.festago.model.StudentVerificationCode
import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequest(
    val code: String,
) {
    companion object {
        fun from(code: StudentVerificationCode) = VerificationRequest(code.value)
    }
}
