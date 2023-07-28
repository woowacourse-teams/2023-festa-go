package com.festago.support;

import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.Ticket;
import java.time.LocalDateTime;

public class MemberTicketFixture {

    private Long id;
    private Member owner = MemberFixture.member().build();
    private Ticket ticket = TicketFixture.ticket().build();
    private int number = 1;
    private LocalDateTime reservedAt = LocalDateTime.now();

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

    public MemberTicketFixture ticket(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public MemberTicketFixture number(int number) {
        this.number = number;
        return this;
    }

    public MemberTicketFixture reservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
        return this;
    }

    public MemberTicket build() {
        return new MemberTicket(id, owner, ticket, number, reservedAt);
    }
}
