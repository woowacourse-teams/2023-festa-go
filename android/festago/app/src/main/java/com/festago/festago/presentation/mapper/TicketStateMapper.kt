package com.festago.festago.presentation.mapper

import com.festago.festago.model.TicketCondition
import com.festago.festago.presentation.model.TicketConditionUiModel

fun TicketCondition.toPresentation(): TicketConditionUiModel =
    when (this) {
        TicketCondition.BEFORE_ENTRY -> TicketConditionUiModel.BEFORE_ENTRY
        TicketCondition.AFTER_ENTRY -> TicketConditionUiModel.AFTER_ENTRY
        TicketCondition.AWAY -> TicketConditionUiModel.AWAY
    }

fun TicketConditionUiModel.toDomain(): TicketCondition =
    when (this) {
        TicketConditionUiModel.BEFORE_ENTRY -> TicketCondition.BEFORE_ENTRY
        TicketConditionUiModel.AFTER_ENTRY -> TicketCondition.AFTER_ENTRY
        TicketConditionUiModel.AWAY -> TicketCondition.AWAY
    }
