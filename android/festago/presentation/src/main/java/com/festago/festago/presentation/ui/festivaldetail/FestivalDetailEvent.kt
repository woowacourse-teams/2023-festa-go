package com.festago.festago.presentation.ui.festivaldetail

sealed interface FestivalDetailEvent {
    class ShowArtistDetail(val artistId: Long) : FestivalDetailEvent
    class ShowSchoolDetail(val schoolId: Long) : FestivalDetailEvent
}
