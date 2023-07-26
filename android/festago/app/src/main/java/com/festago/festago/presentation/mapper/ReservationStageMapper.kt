package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.ReservationStage
import com.festago.festago.presentation.model.ReservationStageUiModel

fun List<ReservationStage>.toPresentation() = map { it.toPresentation() }

fun ReservationStage.toPresentation() = ReservationStageUiModel(
    id = id,
    lineUp = lineUp,
    reservationTickets = reservationTickets.map { it.toPresentation() },
    startTime = startTime,
    ticketOpenTime = ticketOpenTime,
)

fun ReservationStageUiModel.toDomain() = ReservationStage(
    id = id,
    lineUp = lineUp,
    reservationTickets = reservationTickets.map { it.toDomain() },
    startTime = startTime,
    ticketOpenTime = ticketOpenTime,
)
