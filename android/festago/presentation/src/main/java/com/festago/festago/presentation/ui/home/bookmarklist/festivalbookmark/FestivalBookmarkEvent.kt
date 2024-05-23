package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.artistadapter.ArtistUiState
import com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark.uistate.FestivalBookmarkItemUiState

sealed interface FestivalBookmarkEvent {
    class ShowFestivalDetail(val festival: FestivalBookmarkItemUiState) : FestivalBookmarkEvent
    class ShowArtistDetail(val artist: ArtistUiState) : FestivalBookmarkEvent
    object ShowSignIn : FestivalBookmarkEvent
}
