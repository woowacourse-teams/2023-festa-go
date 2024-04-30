package com.festago.festago.presentation.ui.artistdetail.uistate

import com.festago.festago.domain.model.artist.ArtistDetail

sealed interface ArtistDetailUiState {
    object Loading : ArtistDetailUiState

    data class Success(
        val artist: ArtistDetail,
        val festivals: List<FestivalItemUiState>,
        val isLast: Boolean,
    ) : ArtistDetailUiState

    class Error(val refresh: (id: Long) -> Unit) : ArtistDetailUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowEmptyFestivals get() = this is Success && festivals.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
