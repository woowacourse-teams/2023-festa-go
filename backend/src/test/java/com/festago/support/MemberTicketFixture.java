package com.festago.support;

import com.festago.domain.Member;
import com.festago.domain.MemberTicket;
import com.festago.domain.Ticket;

public class MemberTicketFixture {

    private Long id = 1L;
    private Member owner = MemberFixture.member().build();
    private Ticket ticket = TicketFixture.ticket().build();
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

    public MemberTicketFixture ticket(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public MemberTicketFixture number(int number) {
        this.number = number;
        return this;
    }

    public MemberTicket build() {
        return new MemberTicket(id, owner, ticket, number);
    }
}
