package com.festago.festago.presentation.ui.schooldetail

sealed interface SchoolDetailEvent {
    class ShowArtistDetail(val artistId: Long) : SchoolDetailEvent

    class ShowFestivalDetail(val festivalId: Long) : SchoolDetailEvent
}
