package com.festago.festago.presentation.mapper

import com.festago.domain.model.Reservation
import com.festago.festago.presentation.model.ReservationUiModel

fun Reservation.toPresentation() = ReservationUiModel(
    endDate = endDate,
    id = id,
    name = name,
    reservationStages = reservationStages.map { it.toPresentation() },
    startDate = startDate,
    thumbnail = thumbnail,
)

fun ReservationUiModel.toDomain() = Reservation(
    endDate = endDate,
    id = id,
    name = name,
    reservationStages = reservationStages.map { it.toDomain() },
    startDate = startDate,
    thumbnail = thumbnail,
)
