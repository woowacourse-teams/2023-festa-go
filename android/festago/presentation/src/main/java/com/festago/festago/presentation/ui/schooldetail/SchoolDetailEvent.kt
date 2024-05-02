package com.festago.festago.presentation.ui.schooldetail

import com.festago.festago.presentation.ui.schooldetail.uistate.ArtistUiState
import com.festago.festago.presentation.ui.schooldetail.uistate.FestivalItemUiState

sealed interface SchoolDetailEvent {
    class ShowArtistDetail(val artist: ArtistUiState) : SchoolDetailEvent

    class ShowFestivalDetail(val festival: FestivalItemUiState) : SchoolDetailEvent

    class FailedToFetchBookmarkList(val message: String) : SchoolDetailEvent
}
