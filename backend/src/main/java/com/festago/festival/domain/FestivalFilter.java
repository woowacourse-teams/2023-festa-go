package com.festago.festival.domain;

import static com.festago.festival.domain.FestivalSpecification.afterEndDate;
import static com.festago.festival.domain.FestivalSpecification.afterStartDate;
import static com.festago.festival.domain.FestivalSpecification.all;
import static com.festago.festival.domain.FestivalSpecification.beforeEndDate;
import static com.festago.festival.domain.FestivalSpecification.beforeStartDate;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.Function;
import org.springframework.data.jpa.domain.Specification;

public enum FestivalFilter {
    ALL((currentTime) -> all()),
    PROGRESS((currentTime) -> afterStartDate(currentTime)
        .and(beforeEndDate(currentTime))),
    PLANNED((currentTime) -> beforeStartDate(currentTime)),
    END((currentTime) -> afterEndDate(currentTime));

    private final Function<LocalDate, Specification<Festival>> filter;

    FestivalFilter(Function<LocalDate, Specification<Festival>> filter) {
        this.filter = filter;
    }

    public static FestivalFilter from(String filterName) {
        return Arrays.stream(FestivalFilter.values())
            .filter(festivalFilter -> festivalFilter.name().equalsIgnoreCase(filterName))
            .findAny()
            .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_FESTIVAL_FILTER));
    }

    public Specification<Festival> getSpecification() {
        return filter.apply(LocalDate.now());
    }
}
