package com.festago.festago.model

class ReservationTickets(private val tickets: List<ReservationTicket>) {

    fun sortedByTicketTypes(): List<ReservationTicket> {
        val ticketTypes = TicketType.values().toList()
        return tickets.sortedBy { ticketTypes.indexOf(it.ticketType) }
    }
}
