package com.festago.festival.repository;

import com.festago.common.util.Validator;
import java.time.LocalDate;
import java.util.Optional;

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

    public Optional<LocalDate> getLastStartDate() {
        return Optional.ofNullable(lastStartDate);
    }

    public Optional<Long> getLastFestivalId() {
        return Optional.ofNullable(lastFestivalId);
    }

    public Integer getLimit() {
        return limit;
    }
}
