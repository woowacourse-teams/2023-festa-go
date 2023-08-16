package com.festago.festago.data.dto

import com.festago.domain.model.Festival
import kotlinx.serialization.Serializable

@Serializable
data class FestivalsResponse(
    val festivals: List<FestivalResponse>,
) {
    fun toDomain(): List<Festival> = festivals.map { it.toDomain() }
}
