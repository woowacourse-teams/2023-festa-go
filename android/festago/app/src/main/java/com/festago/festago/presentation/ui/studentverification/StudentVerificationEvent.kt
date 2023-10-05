package com.festago.festago.presentation.ui.studentverification

sealed interface StudentVerificationEvent {
    object VerificationTimeOut : StudentVerificationEvent
    object VerificationFailure : StudentVerificationEvent
    object VerificationSuccess : StudentVerificationEvent
    object SendingEmailFailure : StudentVerificationEvent
}
