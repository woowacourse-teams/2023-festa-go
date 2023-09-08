package com.festago.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.domain.School;
import java.util.List;

public record SchoolsResponse(
    List<SchoolResponse> schools) {

    public static SchoolsResponse from(List<School> schools) {
        return schools.stream()
            .map(SchoolResponse::from)
            .collect(collectingAndThen(toList(), SchoolsResponse::new));
    }
}
