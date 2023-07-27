package com.festago.festago.presentation.ui.home.festivallist

sealed interface FestivalListEvent {
    class OpenTicketReserve(val festivalId: Long) : FestivalListEvent
}
