package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

data class ArtistUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetail: (artistId: Long) -> Unit,
)
