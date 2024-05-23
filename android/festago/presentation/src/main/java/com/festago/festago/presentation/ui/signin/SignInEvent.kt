package com.festago.festago.presentation.ui.signin

sealed interface SignInEvent {
    object ShowHome : SignInEvent
    object SignInSuccess : SignInEvent
    object SignInFailure : SignInEvent
}
