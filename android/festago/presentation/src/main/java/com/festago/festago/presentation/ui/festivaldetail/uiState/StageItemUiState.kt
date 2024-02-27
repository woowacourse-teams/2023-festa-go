package com.festago.festago.presentation.ui.festivaldetail.uiState

import com.festago.festago.domain.model.artist.Artist
import java.time.LocalDateTime

data class StageItemUiState(
    val id: Long,
    val startDateTime: LocalDateTime,
    val artists: List<Artist>,
)
