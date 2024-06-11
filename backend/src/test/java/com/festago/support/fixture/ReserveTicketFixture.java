package com.festago.support.fixture;

import com.festago.ticket.domain.NewTicketType;
import com.festago.ticketing.domain.ReserveTicket;
import java.time.LocalDateTime;

public class ReserveTicketFixture extends BaseFixture {

    private Long id;
    private Long memberId = 1L;
    private Long ticketId = 1L;
    private int sequence = 1;
    private LocalDateTime entryTime = LocalDateTime.now();
    private NewTicketType ticketType = NewTicketType.STAGE;

    private ReserveTicketFixture() {
    }

    public static ReserveTicketFixture builder() {
        return new ReserveTicketFixture();
    }

    public ReserveTicketFixture id(Long id) {
        this.id = id;
        return this;
    }

    public ReserveTicketFixture memberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public ReserveTicketFixture ticketId(Long ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    public ReserveTicketFixture sequence(int sequence) {
        this.sequence = sequence;
        return this;
    }

    public ReserveTicketFixture entryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        return this;
    }

    public ReserveTicketFixture ticketType(NewTicketType ticketType) {
        this.ticketType = ticketType;
        return this;
    }

    public ReserveTicket build() {
        return new ReserveTicket(id, memberId, ticketType, ticketId, sequence, entryTime);
    }
}
