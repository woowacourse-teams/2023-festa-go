package com.festago.festago.presentation.ui.search.uistate

data class ArtistUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetailClick: (artist: ArtistUiState) -> Unit,
)
