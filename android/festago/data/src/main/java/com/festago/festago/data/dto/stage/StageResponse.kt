package com.festago.festago.data.dto.stage

import com.festago.festago.data.dto.artist.ArtistResponse
import com.festago.festago.domain.model.stage.Stage
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class StageResponse(
    val id: Long,
    val startDateTime: String,
    val artists: List<ArtistResponse>,
) {
    fun toDomain() = Stage(
        id = id,
        startDateTime = LocalDateTime.parse(startDateTime),
        artists = artists.map { it.toDomain() },
    )
}
