package com.festago.support.fixture;

import com.festago.stage.domain.Stage;
import com.festago.ticket.domain.StageTicket;
import com.festago.ticket.domain.TicketExclusive;

public class StageTicketFixture extends BaseFixture {

    private Long id;

    private Long schoolId;

    private TicketExclusive ticketExclusive = TicketExclusive.NONE;

    private Stage stage = StageFixture.builder().build();

    private StageTicketFixture() {
    }

    public static StageTicketFixture builder() {
        return new StageTicketFixture();
    }

    public StageTicketFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StageTicketFixture schoolId(Long schoolId) {
        this.schoolId = schoolId;
        return this;
    }

    public StageTicketFixture ticketExclusive(TicketExclusive ticketExclusive) {
        this.ticketExclusive = ticketExclusive;
        return this;
    }

    public StageTicketFixture stage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public StageTicket build() {
        if (schoolId == null) {
            schoolId = stage.getFestival().getSchool().getId();
        }
        return new StageTicket(id, schoolId, ticketExclusive, stage);
    }
}
