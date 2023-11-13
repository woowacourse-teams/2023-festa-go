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

    public static Specification<Festival> afterStartDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(START_DATE), currentTime);
    }

    public static Specification<Festival> beforeStartDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(START_DATE), currentTime);
    }

    public static Specification<Festival> afterEndDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(END_DATE), currentTime);
    }

    public static Specification<Festival> beforeEndDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(END_DATE), currentTime);
    }
}
