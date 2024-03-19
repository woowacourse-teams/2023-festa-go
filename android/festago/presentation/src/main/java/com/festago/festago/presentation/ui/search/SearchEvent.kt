package com.festago.festago.presentation.ui.search

sealed interface SearchEvent {
    class ShowFestivalDetail(val festivalId: Long) : SearchEvent
    class ShowArtistDetail(val artistId: Long) : SearchEvent
    class ShowSchoolDetail(val schoolId: Long) : SearchEvent
    object SearchBlank : SearchEvent
}
