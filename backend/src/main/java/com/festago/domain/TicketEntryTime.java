package com.festago.domain;

import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TicketEntryTime extends BaseTimeEntity implements Comparable<TicketEntryTime> {

    private static final int MIN_TOTAL_AMOUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime entryTime;

    @Min(value = 0)
    private int amount;

    protected TicketEntryTime() {
    }

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
        checkNotNull(entryTime);
        checkScope(amount);
        validateAmont(amount);
    }

    private void checkNotNull(LocalDateTime entryTime) {
        if (Objects.isNull(entryTime)) {
            throw new IllegalArgumentException("TicketEntryTime 은 허용되지 않은 null 값으로 생성할 수 없습니다.");
        }
    }

    private void checkScope(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("TicketEntryTime 의 필드로 허용된 범위를 넘은 column 을 넣을 수 없습니다.");
        }
    }

    private void validateAmont(int amount) {
        if (amount < MIN_TOTAL_AMOUNT) {
            throw new BadRequestException(ErrorCode.INVALID_MIN_TICKET_AMOUNT);
        }
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
