package com.festago.support.fixture;

import com.festago.ticket.domain.StageTicketEntryTime;
import java.time.LocalDateTime;

public class StageTicketEntryTimeFixture extends BaseFixture {

    private Long id;
    private Long stageTicketId;
    private LocalDateTime entryTime;
    private int amount;

    public StageTicketEntryTimeFixture() {
    }

    public static StageTicketEntryTimeFixture builder() {
        return new StageTicketEntryTimeFixture();
    }

    public StageTicketEntryTimeFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StageTicketEntryTimeFixture stageTicketId(Long stageTicketId) {
        this.stageTicketId = stageTicketId;
        return this;
    }

    public StageTicketEntryTimeFixture entryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        return this;
    }

    public StageTicketEntryTimeFixture amount(int amount) {
        this.amount = amount;
        return this;
    }

    public StageTicketEntryTime build() {
        return new StageTicketEntryTime(id, stageTicketId, entryTime, amount);
    }
}
