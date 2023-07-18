package com.festago.festago.presentation.model

import androidx.annotation.StringRes
import com.festago.festago.R

enum class TicketStateUiModel(@StringRes val stateName: Int) {

    BEFORE_ENTRY(R.string.all_ticket_state_before_entry),
    AFTER_ENTRY(R.string.all_ticket_state_after_entry),
    AWAY(R.string.all_ticket_state_away),
    EMPTY(R.string.all_ticket_state_before_entry),
}
