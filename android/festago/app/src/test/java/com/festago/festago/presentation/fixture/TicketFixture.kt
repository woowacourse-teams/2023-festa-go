package com.festago.festago.presentation.fixture

import com.festago.festago.domain.model.MemberTicketFestival
import com.festago.festago.domain.model.Stage
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCondition
import java.time.LocalDateTime

object TicketFixture {
    fun getMemberTicket(ticketId: Long = 0): Ticket {
        return Ticket(
            id = ticketId,
            number = 0,
            entryTime = LocalDateTime.MIN,
            condition = TicketCondition.AWAY,
            reserveAt = LocalDateTime.MAX,
            stage = getStage(),
            festivalTicket = getMemberTicketFestival(),
        )
    }

    fun getStage(stageId: Int = 0) = Stage(id = stageId, startTime = LocalDateTime.MIN)

    fun getMemberTicketFestival(festivalId: Int = 0) = MemberTicketFestival(
        id = festivalId,
        name = "test",
        thumbnail = "test.com",
    )

    fun getMemberTickets(ids: List<Long>) = ids.map { id ->
        getMemberTicket(id)
    }
}
