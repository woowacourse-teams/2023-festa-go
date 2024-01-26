package com.festago.festival.repository;

import com.festago.common.util.Validator;
import java.time.LocalDate;

public class FestivalPageable {

    private final LocalDate lastStartDate;
    private final Long lastFestivalId;
    private final Integer limit;

    public FestivalPageable(LocalDate lastStartDate, Long lastFestivalId, Integer limit) {
        validate(limit);
        this.lastStartDate = lastStartDate;
        this.lastFestivalId = lastFestivalId;
        this.limit = limit;
    }

    private void validate(Integer limit) {
        Validator.notNull(limit, "Pagination의 크기는 null일 수 없습니다.");
    }

    public LocalDate getLastStartDate() {
        return lastStartDate;
    }

    public Long getLastFestivalId() {
        return lastFestivalId;
    }

    public Integer getLimit() {
        return limit;
    }
}
