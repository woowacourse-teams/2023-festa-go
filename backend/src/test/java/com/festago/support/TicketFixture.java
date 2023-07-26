package com.festago.support;

import com.festago.domain.Stage;
import com.festago.domain.Ticket;
import com.festago.domain.TicketType;
import java.time.LocalDateTime;

public class TicketFixture {

    private Long id;
    private Stage stage = StageFixture.stage().build();
    private TicketType ticketType = TicketType.VISITOR;
    private Integer totalAmount = 100;
    private LocalDateTime entryTime = LocalDateTime.now();

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

    public TicketFixture totalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }
    
    public TicketFixture entryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        return this;
    }

    public Ticket build() {
        return new Ticket(id, stage, ticketType, totalAmount, entryTime);
    }
}
