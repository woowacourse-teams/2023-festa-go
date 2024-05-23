package com.festago.ticket.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketEntryTime extends BaseTimeEntity implements Comparable<TicketEntryTime> {

    private static final int MIN_AMOUNT_VALUE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime entryTime;

    @Min(value = MIN_AMOUNT_VALUE)
    private int amount;

    public TicketEntryTime(LocalDateTime entryTime, int amount) {
        this(null, entryTime, amount);
    }

    private TicketEntryTime(Long id, LocalDateTime entryTime, int amount) {
        validate(entryTime, amount);
        this.id = id;
        this.entryTime = entryTime;
        this.amount = amount;
    }

    private void validate(LocalDateTime entryTime, int amount) {
        validateEntryTime(entryTime);
        validateAmount(amount);
    }

    private void validateEntryTime(LocalDateTime entryTime) {
        Validator.notNull(entryTime, "entryTime");
    }

    private void validateAmount(int amount) {
        Validator.minValue(amount, MIN_AMOUNT_VALUE, "amount");
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public int compareTo(TicketEntryTime o) {
        return entryTime.compareTo(o.getEntryTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketEntryTime that)) {
            return false;
        }

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
