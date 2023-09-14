package com.festago.support;

import com.festago.member.domain.Member;
import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.TicketType;
import com.festago.ticketing.domain.MemberTicket;
import java.time.LocalDateTime;

public class MemberTicketFixture {

    private Long id;
    private Member owner = MemberFixture.member().build();
    private Stage stage = StageFixture.stage().build();
    private TicketType ticketType = TicketType.VISITOR;
    private LocalDateTime entryTime = LocalDateTime.now();
    private int number = 1;

    private MemberTicketFixture() {
    }

    public static MemberTicketFixture memberTicket() {
        return new MemberTicketFixture();
    }

    public MemberTicketFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MemberTicketFixture owner(Member owner) {
        this.owner = owner;
        return this;
    }

    public MemberTicketFixture stage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public MemberTicketFixture number(int number) {
        this.number = number;
        return this;
    }

    public MemberTicketFixture entryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        return this;
    }

    public MemberTicketFixture ticketType(TicketType ticketType) {
        this.ticketType = ticketType;
        return this;
    }

    public MemberTicket build() {
        return new MemberTicket(id, owner, stage, number, entryTime, ticketType);
    }
}
