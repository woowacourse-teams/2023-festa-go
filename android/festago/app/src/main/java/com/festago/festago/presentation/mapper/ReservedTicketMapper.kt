package com.festago.festago.presentation.mapper

import com.festago.festago.model.ReservedTicket
import com.festago.festago.presentation.model.ReservedTicketUiModel

fun ReservedTicket.toPresentation() = ReservedTicketUiModel(
    ticketId = id,
    number = number,
    entryTime = entryTime,
)
