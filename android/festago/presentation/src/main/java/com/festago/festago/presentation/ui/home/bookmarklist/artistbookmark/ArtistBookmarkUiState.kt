package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

data class ArtistBookmarkUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetail: (artist: ArtistBookmarkUiState) -> Unit,
)
