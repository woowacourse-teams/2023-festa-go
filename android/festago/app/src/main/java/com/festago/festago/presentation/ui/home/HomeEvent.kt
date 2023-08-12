package com.festago.festago.presentation.ui.home

sealed interface HomeEvent {
    object ShowFestivalList : HomeEvent
    object ShowTicketList : HomeEvent
    object ShowMyPage : HomeEvent
    object ShowSignIn : HomeEvent
}
