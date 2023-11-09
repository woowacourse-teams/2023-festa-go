package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FestivalSpecification {

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
