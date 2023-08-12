package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.ReservationStage
import com.festago.festago.presentation.model.ReservationStageUiModel
import com.festago.festago.presentation.model.TicketReserveItemUiModel
import java.time.LocalDateTime

fun List<ReservationStage>.toPresentation() = map { it.toPresentation() }

fun ReservationStage.toPresentation() = ReservationStageUiModel(
    id = id,
    lineUp = lineUp,
    reservationTickets = reservationTickets.map { it.toPresentation() },
    startTime = startTime,
    ticketOpenTime = ticketOpenTime,
    canReserve = LocalDateTime.now().isAfter(ticketOpenTime),
)

fun ReservationStageUiModel.toDomain() = ReservationStage(
    id = id,
    lineUp = lineUp,
    reservationTickets = reservationTickets.map { it.toDomain() },
    startTime = startTime,
    ticketOpenTime = ticketOpenTime,
)

fun ReservationStageUiModel.toTicketReserveItem(isSigned: Boolean) = TicketReserveItemUiModel(
    id = id,
    lineUp = lineUp,
    startTime = startTime,
    ticketOpenTime = ticketOpenTime,
    reservationTickets = reservationTickets,
    canReserve = canReserve,
    isSigned = isSigned,
)

fun List<ReservationStageUiModel>.toTicketReserveItem(isSigned: Boolean) = map {
    it.toTicketReserveItem(isSigned)
}

fun TicketReserveItemUiModel.toPresentation() = ReservationStageUiModel(
    id = id,
    lineUp = lineUp,
    startTime = startTime,
    ticketOpenTime = ticketOpenTime,
    reservationTickets = reservationTickets,
    canReserve = canReserve,
)
