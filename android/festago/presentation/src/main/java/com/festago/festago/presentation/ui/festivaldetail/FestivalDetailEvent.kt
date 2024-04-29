package com.festago.festago.presentation.ui.festivaldetail

import com.festago.festago.domain.model.school.School
import com.festago.festago.presentation.ui.festivaldetail.uiState.ArtistItemUiState

sealed interface FestivalDetailEvent {
    class ShowArtistDetail(val artist: ArtistItemUiState) : FestivalDetailEvent
    class ShowSchoolDetail(val school: School) : FestivalDetailEvent
}
