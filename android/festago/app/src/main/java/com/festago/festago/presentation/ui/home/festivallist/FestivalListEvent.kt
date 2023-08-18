package com.festago.festago.presentation.ui.home.festivallist

sealed interface FestivalListEvent {
    class ShowTicketReserve(val festivalId: Long) : FestivalListEvent
}
