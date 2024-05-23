package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.artistadapter

data class ArtistUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetail: (artist: ArtistUiState) -> Unit,
)
