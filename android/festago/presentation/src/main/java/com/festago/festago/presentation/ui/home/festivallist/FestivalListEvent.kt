package com.festago.festago.presentation.ui.home.festivallist

sealed interface FestivalListEvent {
    class ShowFestivalDetail(val festivalId: Long) : FestivalListEvent
}
