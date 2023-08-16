package com.festago.festago.data.dto

import com.festago.festago.model.Stage
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class StageResponse(
    val id: Int,
    val startTime: String,
) {
    fun toDomain(): Stage = Stage(
        id = id,
        startTime = LocalDateTime.parse(startTime),
    )
}
