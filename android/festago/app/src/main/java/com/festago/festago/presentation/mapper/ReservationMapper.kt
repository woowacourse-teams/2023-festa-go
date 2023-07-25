package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.Reservation
import com.festago.festago.presentation.model.ReservationUiModel

fun List<Reservation>.toPresentation() = map { it.toPresentation() }

fun Reservation.toPresentation() = ReservationUiModel(
    id = id,
)

fun ReservationUiModel.toDomain() = Reservation(
    id = id,
)
