package com.festago.festival.domain;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class FestivalSpecification {

    private FestivalSpecification() {
    }

    public static Specification<Festival> all() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Festival> afterStartDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("startDate"), currentTime);
    }

    public static Specification<Festival> beforeStartDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("startDate"), currentTime);
    }

    public static Specification<Festival> afterEndDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("endDate"), currentTime);
    }

    public static Specification<Festival> beforeEndDate(LocalDate currentTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("endDate"), currentTime);
    }
}
