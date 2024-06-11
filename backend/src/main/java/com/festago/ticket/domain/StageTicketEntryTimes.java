package com.festago.ticket.domain;

import static java.util.Comparator.comparing;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class StageTicketEntryTimes {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "stageTicketId")
    private List<StageTicketEntryTime> ticketEntryTimes = new ArrayList<>();

    public LocalDateTime calculateEntryTime(int sequence) {
        Validator.minValue(sequence, 1, "sequence");
        int lastSequence = 0;
        ticketEntryTimes.sort(comparing(StageTicketEntryTime::getEntryTime));
        for (StageTicketEntryTime ticketEntryTime : ticketEntryTimes) {
            lastSequence += ticketEntryTime.getAmount();
            if (sequence <= lastSequence) {
                return ticketEntryTime.getEntryTime();
            }
        }
        throw new BadRequestException(ErrorCode.TICKET_SOLD_OUT);
    }

    public void add(StageTicketEntryTime stageTicketEntryTime) {
        ticketEntryTimes.add(stageTicketEntryTime);
    }

    public int getTotalAmount() {
        return ticketEntryTimes.stream()
            .mapToInt(StageTicketEntryTime::getAmount)
            .sum();
    }

    public boolean remove(LocalDateTime entryTime) {
        return ticketEntryTimes.removeIf(it -> Objects.equals(it.getEntryTime(), entryTime));
    }

    public boolean isEmpty() {
        return ticketEntryTimes.isEmpty();
    }
}
