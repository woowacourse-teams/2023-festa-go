package com.festago.support.fixture;

import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;

@Deprecated
public class TicketFixture {

    private Long id;
    private Stage stage = StageFixture.builder().build();
    private TicketType ticketType = TicketType.VISITOR;
    private Long schoolId = 1L;

    private TicketFixture() {
    }

    public static TicketFixture builder() {
        return new TicketFixture();
    }

    public TicketFixture id(Long id) {
        this.id = id;
        return this;
    }

    public TicketFixture stage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public TicketFixture ticketType(TicketType ticketType) {
        this.ticketType = ticketType;
        return this;
    }

    public TicketFixture schoolId(Long schoolId) {
        this.schoolId = schoolId;
        return this;
    }

    public Ticket build() {
        return new Ticket(id, stage, ticketType, schoolId);
    }
}
