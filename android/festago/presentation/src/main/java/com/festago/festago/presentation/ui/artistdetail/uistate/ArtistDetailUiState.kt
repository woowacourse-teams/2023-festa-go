package com.festago.festago.presentation.ui.artistdetail.uistate

import com.festago.festago.domain.model.artist.ArtistDetail

sealed interface ArtistDetailUiState {
    object Loading : ArtistDetailUiState

    object Error : ArtistDetailUiState

    data class Success(
        val artist: ArtistDetail,
        val festivals: List<FestivalItemUiState>,
    ) : ArtistDetailUiState

    val shouldShowEmptyFestivals get() = this is Success && festivals.isEmpty()
}
