package com.festago.festago.presentation.ui.artistdetail

sealed interface ArtistDetailEvent {
    class ShowArtistDetail(val artistId: Long) : ArtistDetailEvent

    class ShowFestivalDetail(val festivalId: Long) : ArtistDetailEvent
}
