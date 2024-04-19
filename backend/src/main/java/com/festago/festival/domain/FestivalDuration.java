package com.festago.festival.domain;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalDuration {

    private LocalDate startDate;
    private LocalDate endDate;

    public FestivalDuration(LocalDate startDate, LocalDate endDate) {
        validate(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validate(LocalDate startDate, LocalDate endDate) {
        Validator.notNull(startDate, "startDate");
        Validator.notNull(endDate, "endDate");
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_DURATION);
        }
    }

    public boolean isStartDateBeforeTo(LocalDate date) {
        return startDate.isBefore(date);
    }

    public boolean isNotInDuration(LocalDate date) {
        return date.isBefore(startDate) || date.isAfter(endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
