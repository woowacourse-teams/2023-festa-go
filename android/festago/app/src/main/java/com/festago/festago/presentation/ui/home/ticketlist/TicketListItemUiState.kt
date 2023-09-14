package com.festago.festago.presentation.ui.home.ticketlist

import com.festago.festago.model.Stage
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCondition
import java.time.LocalDateTime

data class TicketListItemUiState(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val reserveAt: LocalDateTime,
    val condition: TicketCondition,
    val stage: Stage,
    val festivalId: Int,
    val festivalName: String,
    val festivalThumbnail: String,
    val canEntry: Boolean,
    val onTicketEntry: (ticketId: Long) -> Unit,
) {
    companion object {
        fun of(ticket: Ticket, onTicketEntry: (ticketId: Long) -> Unit) = TicketListItemUiState(
            id = ticket.id,
            number = ticket.number,
            entryTime = ticket.entryTime,
            reserveAt = ticket.reserveAt,
            condition = ticket.condition,
            stage = ticket.stage,
            festivalId = ticket.festivalTicket.id,
            festivalName = ticket.festivalTicket.name,
            festivalThumbnail = ticket.festivalTicket.thumbnail,
            canEntry = LocalDateTime.now().isAfter(ticket.entryTime),
            onTicketEntry = onTicketEntry,
        )
    }
}
