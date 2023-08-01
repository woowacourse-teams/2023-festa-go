package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.TicketState
import com.festago.festago.presentation.model.TicketStateUiModel

fun TicketState.toPresentation(): TicketStateUiModel =
    when (this) {
        TicketState.BEFORE_ENTRY -> TicketStateUiModel.BEFORE_ENTRY
        TicketState.AFTER_ENTRY -> TicketStateUiModel.AFTER_ENTRY
        TicketState.AWAY -> TicketStateUiModel.AWAY
    }

fun TicketStateUiModel.toDomain(): TicketState =
    when (this) {
        TicketStateUiModel.BEFORE_ENTRY -> TicketState.BEFORE_ENTRY
        TicketStateUiModel.AFTER_ENTRY -> TicketState.AFTER_ENTRY
        TicketStateUiModel.AWAY -> TicketState.AWAY
        else -> throw IllegalStateException()
    }
