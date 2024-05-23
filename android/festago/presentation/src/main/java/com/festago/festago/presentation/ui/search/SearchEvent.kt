package com.festago.festago.presentation.ui.search

import com.festago.festago.presentation.ui.search.uistate.ArtistUiState
import com.festago.festago.presentation.ui.search.uistate.FestivalSearchItemUiState
import com.festago.festago.presentation.ui.search.uistate.SchoolSearchItemUiState

sealed interface SearchEvent {
    class ShowFestivalDetail(val festival: FestivalSearchItemUiState) : SearchEvent
    class ShowArtistDetail(val artist: ArtistUiState) : SearchEvent
    class ShowSchoolDetail(val school: SchoolSearchItemUiState) : SearchEvent
    class UpdateSearchQuery(val searchQuery: String) : SearchEvent
    object SearchBlank : SearchEvent
}
