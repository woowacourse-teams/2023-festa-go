package com.festago.festival.dto.v1;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.repository.FestivalFilter;
import com.festago.school.domain.SchoolRegion;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class FestivalV1lListRequest {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final SchoolRegion location;
    private final FestivalFilter filter;
    private final Integer limit;
    private final Long lastFestivalId;
    private final LocalDate lastStartDate;

    public FestivalV1lListRequest(String location, String filter, Integer limit, Long lastFestivalId,
                                  String lastStartDate) {
        validate(location, filter, limit, lastFestivalId, lastStartDate);
        this.location = convertToRegion(location);
        this.filter = converToFilter(filter);
        this.limit = limit == null ? DEFAULT_PAGE_SIZE : limit;
        this.lastFestivalId = lastFestivalId;
        this.lastStartDate = convertLocalDate(lastStartDate);
    }

    private static void existTogetherOrNot(Long lastFestivalId, String lastStartDate) {
        if (lastFestivalId == null && lastStartDate == null) {
            return;
        }
        if (lastFestivalId != null && lastStartDate != null) {
            return;
        }
        throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_LIST_INDEX);
    }

    private void validate(String location, String filter, Integer limit, Long lastFestivalId, String lastStartDate) {
        existTogetherOrNot(lastFestivalId, lastStartDate);
        validateRange(limit);
    }

    private void validateRange(Integer limit) {
        if (limit == null) {
            return;
        }
        if (limit < MIN_PAGE_SIZE || limit > MAX_PAGE_SIZE) {
            throw new BadRequestException(ErrorCode.INVALID_PAGE_SIZE);
        }
    }

    private SchoolRegion convertToRegion(String location) {
        if (location == null) {
            return SchoolRegion.기타;
        }
        return SchoolRegion.from(location);
    }

    private FestivalFilter converToFilter(String filter) {
        if (filter == null) {
            return FestivalFilter.PROGRESS;
        }
        return FestivalFilter.from(filter);
    }

    private LocalDate convertLocalDate(String lastStartDate) {
        if (lastStartDate == null) {
            return null;
        }
        try {
            return LocalDate.parse(lastStartDate);
        } catch (DateTimeParseException e) {
            throw new BadRequestException(ErrorCode.INVALID_LOCAL_DATE);
        }
    }

    public SchoolRegion getLocation() {
        return location;
    }

    public FestivalFilter getFilter() {
        return filter;
    }

    public Integer getLimit() {
        return limit;
    }

    public Optional<Long> getLastFestivalId() {
        return Optional.ofNullable(lastFestivalId);
    }

    public Optional<LocalDate> getLastStartDate() {
        return Optional.ofNullable(lastStartDate);
    }
}
