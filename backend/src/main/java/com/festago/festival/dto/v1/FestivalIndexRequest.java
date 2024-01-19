package com.festago.festival.dto.v1;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import java.time.LocalDate;

public class FestivalIndexRequest {

    private final Long lastFestivalId;
    private final LocalDate lastStartDate;

    public FestivalIndexRequest(Long lastFestivalId, String lastStartDate) {
        validate(lastFestivalId, lastStartDate);
        this.lastFestivalId = lastFestivalId;
        this.lastStartDate = convertLocalDate(lastStartDate);
    }

    private LocalDate convertLocalDate(String lastStartDate) {
        if (lastStartDate == null) {
            return null;
        }
        return LocalDate.parse(lastStartDate);
    }

    private void validate(Long lastFestivalId, String lastStartDate) {
        if (lastFestivalId == null && lastStartDate == null) {
            return;
        }
        if (lastFestivalId != null && lastStartDate != null) {
            return;
        }
        throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_LIST_INDEX);
    }

    public Long getLastFestivalId() {
        return lastFestivalId;
    }

    public LocalDate getLastStartDate() {
        return lastStartDate;
    }

    @Override
    public String toString() {
        return "FestivalIndexRequest{" +
            "lastFestivalId=" + lastFestivalId +
            ", lastStartDate=" + lastStartDate +
            '}';
    }
}
