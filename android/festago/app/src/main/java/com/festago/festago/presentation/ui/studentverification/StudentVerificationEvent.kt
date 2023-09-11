package com.festago.festago.presentation.ui.studentverification

sealed interface StudentVerificationEvent {
    object CodeTimeOut : StudentVerificationEvent
    object VerificationFailure : StudentVerificationEvent
    object VerificationSuccess : StudentVerificationEvent
}
