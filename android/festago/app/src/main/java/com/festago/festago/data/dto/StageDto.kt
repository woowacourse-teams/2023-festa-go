package com.festago.festago.data.dto

import com.festago.festago.domain.model.Stage
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class StageDto(
    val id: Int,
    val name: String,
    val startTime: String,
) {
    fun toDomain(): Stage {
        return Stage(
            id = id.toLong(),
            name = name,
            startTime = LocalDateTime.parse(startTime),
        )
    }
}
