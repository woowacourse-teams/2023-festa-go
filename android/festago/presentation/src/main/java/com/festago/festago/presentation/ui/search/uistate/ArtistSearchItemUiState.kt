package com.festago.festago.presentation.ui.search.uistate

data class ArtistSearchItemUiState(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val todayStage: Int,
    val upcomingStage: Int,
    val onArtistDetailClick: (ArtistUiState) -> Unit,
) {
    fun toArtistUiState() = ArtistUiState(
        id,
        name,
        profileImageUrl,
        onArtistDetailClick,
    )
}
