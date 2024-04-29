package com.festago.festago.presentation.ui.home.festivallist

import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState

sealed interface FestivalListEvent {
    class ShowFestivalDetail(val festival: FestivalItemUiState) : FestivalListEvent
}
