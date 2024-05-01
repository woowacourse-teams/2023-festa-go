package com.festago.festago.presentation.ui.signin

sealed interface SignInEvent {
    object ShowHome : SignInEvent
    object SignInFailure : SignInEvent
}
