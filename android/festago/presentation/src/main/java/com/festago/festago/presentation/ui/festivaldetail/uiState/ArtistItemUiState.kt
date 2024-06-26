package com.festago.festago.presentation.ui.festivaldetail.uiState

data class ArtistItemUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetail: (artist: ArtistItemUiState) -> Unit,
)
