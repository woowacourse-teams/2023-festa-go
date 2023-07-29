package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.Ticket
import com.festago.festago.presentation.model.TicketUiModel

fun Ticket.toPresentation() = TicketUiModel(
    id = id,
    number = number,
    entryTime = entryTime,
    ticketState = state.toPresentation(),
    stage = stage.toPresentation(),
    imageUrl = imageUrl,
)

fun List<Ticket>.toPresentation() = this.map { ticket -> ticket.toPresentation() }

fun TicketUiModel.toDomain() = Ticket(
    id = id,
    number = number,
    entryTime = entryTime,
    state = ticketState.toDomain(),
    stage = stage.toDomain(),
    imageUrl = imageUrl,
)
