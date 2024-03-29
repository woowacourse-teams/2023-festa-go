package com.festago.festago.presentation.ui.artistdetail.uistate

data class ArtistUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetailClick: (Long) -> Unit,
)
