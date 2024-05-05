package com.festago.festago.presentation.ui.artistdetail

import com.festago.festago.presentation.ui.artistdetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.artistdetail.uistate.FestivalItemUiState

sealed interface ArtistDetailEvent {
    class ShowArtistDetail(val artist: ArtistUiState) : ArtistDetailEvent
    class ShowFestivalDetail(val festival: FestivalItemUiState) : ArtistDetailEvent
    class BookmarkSuccess(val isBookmarked: Boolean) : ArtistDetailEvent
    class BookmarkFailure(val message: String) : ArtistDetailEvent
}
