package com.festago.support;

import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.Ticket;
import com.festago.ticket.domain.TicketType;

public class TicketFixture {

    private Long id;
    private Stage stage = StageFixture.stage().build();
    private TicketType ticketType = TicketType.VISITOR;

    private TicketFixture() {
    }

    public static TicketFixture ticket() {
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

    public Ticket build() {
        return new Ticket(id, stage, ticketType);
    }
}
