package com.festago.festago.presentation.ui.artistdetail.uistate

import com.festago.festago.domain.model.artist.ArtistDetail
import com.festago.festago.domain.model.artist.Stages

sealed interface ArtistDetailUiState {
    object Loading : ArtistDetailUiState
    object Error : ArtistDetailUiState
    data class Success(
        val artist: ArtistDetail,
        val stages: List<StageUiState>,
    ) : ArtistDetailUiState
}