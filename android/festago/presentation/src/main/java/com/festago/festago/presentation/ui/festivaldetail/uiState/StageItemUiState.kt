package com.festago.festago.presentation.ui.festivaldetail.uiState

import java.time.LocalDateTime

data class StageItemUiState(
    val id: Long,
    val startDateTime: LocalDateTime,
    val artists: List<ArtistItemUiState>,
)
