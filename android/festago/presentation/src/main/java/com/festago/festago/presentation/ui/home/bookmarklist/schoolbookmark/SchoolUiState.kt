package com.festago.festago.presentation.ui.home.bookmarklist.schoolbookmark

data class SchoolUiState(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val onArtistDetail: (artistId: Long) -> Unit,
)
