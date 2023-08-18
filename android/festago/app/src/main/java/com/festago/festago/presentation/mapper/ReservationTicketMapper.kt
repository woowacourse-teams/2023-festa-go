package com.festago.festago.presentation.mapper

import com.festago.festago.model.ReservationTicket
import com.festago.festago.presentation.model.ReservationTicketUiModel

fun ReservationTicket.toPresentation() = ReservationTicketUiModel(
    id = id,
    remainAmount = remainAmount,
    ticketType = ticketType,
    totalAmount = totalAmount,
)

fun ReservationTicketUiModel.toDomain() = ReservationTicket(
    id = id,
    remainAmount = remainAmount,
    ticketType = ticketType,
    totalAmount = totalAmount,
)
