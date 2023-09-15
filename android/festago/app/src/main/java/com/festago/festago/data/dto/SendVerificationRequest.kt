package com.festago.festago.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendVerificationRequest(
    val userName: String,
    val schoolId: Int,
)
