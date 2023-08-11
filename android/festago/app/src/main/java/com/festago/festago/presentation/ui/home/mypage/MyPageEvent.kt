package com.festago.festago.presentation.ui.home.mypage

sealed interface MyPageEvent {
    object ShowSignIn : MyPageEvent
    object SignOutSuccess : MyPageEvent
    object DeleteAccountSuccess : MyPageEvent
    object ShowTicketHistory : MyPageEvent
}
