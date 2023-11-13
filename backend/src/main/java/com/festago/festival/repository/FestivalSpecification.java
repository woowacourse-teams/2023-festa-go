package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FestivalSpecification {

    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    public static Specification<Festival> progress(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.lessThanOrEqualTo(root.get(START_DATE), currentTime),
            criteriaBuilder.greaterThanOrEqualTo(root.get(END_DATE), currentTime)
        );
    }

    public static Specification<Festival> planned(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(START_DATE), currentTime);
    }

    public static Specification<Festival> end(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(END_DATE), currentTime);
    }

}
