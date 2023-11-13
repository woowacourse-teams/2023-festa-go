package com.festago.festival.repository;

import static com.festago.festival.repository.FestivalSpecification.afterEndDate;
import static com.festago.festival.repository.FestivalSpecification.afterStartDate;
import static com.festago.festival.repository.FestivalSpecification.beforeEndDate;
import static com.festago.festival.repository.FestivalSpecification.beforeStartDate;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.festival.domain.Festival;
import java.time.LocalDate;
import java.util.function.Function;
import org.springframework.data.jpa.domain.Specification;

public enum FestivalFilter {
    PROGRESS(currentTime -> afterStartDate(currentTime)
        .and(beforeEndDate(currentTime))),
    PLANNED(currentTime -> beforeStartDate(currentTime)),
    END(currentTime -> afterEndDate(currentTime));

    private final Function<LocalDate, Specification<Festival>> filter;

    FestivalFilter(Function<LocalDate, Specification<Festival>> filter) {
        this.filter = filter;
    }

    public static FestivalFilter from(String filterName) {
        return switch (filterName.toUpperCase()) {
            case "PROGRESS" -> PROGRESS;
            case "PLANNED" -> PLANNED;
            case "END" -> END;
            default -> throw new BadRequestException(ErrorCode.INVALID_FESTIVAL_FILTER);
        };
    }

    public Specification<Festival> getSpecification() {
        return filter.apply(LocalDate.now());
    }
}
