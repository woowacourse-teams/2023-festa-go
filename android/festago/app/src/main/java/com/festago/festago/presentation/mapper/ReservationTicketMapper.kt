package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.presentation.model.ReservationTicketUiModel

fun ReservationTicket.toPresentation() = ReservationTicketUiModel(
    remainAmount = remainAmount,
    ticketType = ticketType,
    totalAmount = totalAmount,
)

fun ReservationTicketUiModel.toDomain() = ReservationTicket(
    remainAmount = remainAmount,
    ticketType = ticketType,
    totalAmount = totalAmount,
)
