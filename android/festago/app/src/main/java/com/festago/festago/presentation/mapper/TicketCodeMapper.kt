package com.festago.festago.presentation.mapper

import com.festago.festago.domain.model.TicketCode
import com.festago.festago.presentation.model.TicketCodeUiModel

fun TicketCode.toPresentation(): TicketCodeUiModel = TicketCodeUiModel(code = code, period = period)

fun TicketCodeUiModel.toDomain(): TicketCode = TicketCode(code = code, period = period)
