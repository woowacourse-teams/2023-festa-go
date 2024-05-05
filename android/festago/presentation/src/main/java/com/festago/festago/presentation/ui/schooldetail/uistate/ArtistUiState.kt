package com.festago.festago.presentation.ui.schooldetail.uistate

data class ArtistUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetailClick: (ArtistUiState) -> Unit,
)
