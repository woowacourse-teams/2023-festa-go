package com.festago.festago.presentation.ui.ticketentry

import com.festago.festago.R
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCode
import com.festago.festago.model.TicketCondition.AFTER_ENTRY
import com.festago.festago.model.TicketCondition.AWAY
import com.festago.festago.model.TicketCondition.BEFORE_ENTRY

sealed interface TicketEntryUiState {
    object Loading : TicketEntryUiState

    data class Success(
        val ticket: Ticket,
        val ticketCode: TicketCode,
        val remainTime: Int,
    ) : TicketEntryUiState {
        val ticketBackgroundId: Int
            get() = when (ticket.condition) {
                BEFORE_ENTRY -> R.drawable.bg_ticket_gradient_primary
                AFTER_ENTRY -> R.drawable.bg_ticket_gradient_secondary
                AWAY -> R.drawable.bg_ticket_gradient_tertiary
            }
        val progressBarBackgroundId: Int
            get() = when (ticket.condition) {
                BEFORE_ENTRY -> R.drawable.pb_ticket_remain_time_primary
                AFTER_ENTRY -> R.drawable.pb_ticket_remain_time_secondary
                AWAY -> R.drawable.pb_ticket_remain_time_tertiary
            }
        val ticketConditionBackgroundId: Int
            get() = when (ticket.condition) {
                BEFORE_ENTRY -> R.drawable.btn_circle_primary
                AFTER_ENTRY -> R.drawable.btn_circle_secondary
                AWAY -> R.drawable.btn_circle_tertiary
            }
        val ticketConditionColor: Int
            get() = when (ticket.condition) {
                BEFORE_ENTRY -> R.color.md_theme_light_primary
                AFTER_ENTRY -> R.color.md_theme_light_secondary
                AWAY -> R.color.md_theme_light_tertiary
            }
        val ticketConditionTextId: Int
            get() = when (ticket.condition) {
                BEFORE_ENTRY -> R.string.ticket_entry_tv_before_entry
                AFTER_ENTRY -> R.string.ticket_entry_tv_after_entry
                AWAY -> R.string.ticket_entry_tv_away
            }
    }

    object Error : TicketEntryUiState

    val shouldShowSuccess get() = this is Success
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
