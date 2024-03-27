package com.festago.festago.presentation.ui.festivaldetail

sealed interface FestivalDetailEvent {
    class ShowArtistDetail(val artistId: Long) : FestivalDetailEvent
}
