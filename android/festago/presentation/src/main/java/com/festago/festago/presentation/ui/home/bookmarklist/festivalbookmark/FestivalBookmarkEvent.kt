package com.festago.festago.presentation.ui.home.bookmarklist.festivalbookmark

sealed interface FestivalBookmarkEvent {
    class ShowFestivalDetail(val festivalId: Long) : FestivalBookmarkEvent
    class ShowArtistDetail(val artistId: Long) : FestivalBookmarkEvent
}
