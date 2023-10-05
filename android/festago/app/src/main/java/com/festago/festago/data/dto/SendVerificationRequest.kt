package com.festago.festago.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendVerificationRequest(
    val username: String,
    val schoolId: Long,
)
