package com.festago.festago.presentation.ui.home.bookmarklist.artistbookmark

sealed interface ArtistBookmarkEvent {
    class ShowArtistDetail(val artist: ArtistBookmarkUiState) : ArtistBookmarkEvent
    object ShowSignIn : ArtistBookmarkEvent
}
