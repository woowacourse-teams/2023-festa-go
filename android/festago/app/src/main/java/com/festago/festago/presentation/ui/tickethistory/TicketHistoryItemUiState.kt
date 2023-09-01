package com.festago.festago.presentation.ui.tickethistory

import com.festago.festago.model.Stage
import com.festago.festago.model.Ticket
import java.time.LocalDateTime

data class TicketHistoryItemUiState(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val reserveAt: LocalDateTime,
    val stage: Stage,
    val festivalId: Int,
    val festivalName: String,
    val festivalThumbnail: String,
) {
    companion object {
        fun from(ticket: Ticket) = TicketHistoryItemUiState(
            id = ticket.id,
            number = ticket.number,
            entryTime = ticket.entryTime,
            reserveAt = ticket.reserveAt,
            stage = ticket.stage,
            festivalId = ticket.festivalTicket.id,
            festivalName = ticket.festivalTicket.name,
            festivalThumbnail = ticket.festivalTicket.thumbnail,
        )
    }
}
