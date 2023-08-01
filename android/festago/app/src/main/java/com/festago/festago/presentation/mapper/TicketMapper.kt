package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.Ticket
import com.festago.festago.presentation.model.TicketUiModel

fun Ticket.toPresentation() = TicketUiModel(
    id = id,
    number = number,
    entryTime = entryTime,
    ticketState = state.toPresentation(),
    stage = stage.toPresentation(),
    reserveAt = reserveAt,
    festivalId = festivalTicket.id,
    festivalName = festivalTicket.name,
    festivalThumbnail = festivalTicket.thumbnail,
)

fun List<Ticket>.toPresentation() = this.map { ticket -> ticket.toPresentation() }
