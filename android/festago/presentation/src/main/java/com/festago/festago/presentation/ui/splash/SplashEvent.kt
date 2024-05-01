package com.festago.festago.presentation.ui.splash

sealed interface SplashEvent {
    object ShowSignIn : SplashEvent
    object ShowHome : SplashEvent
}
