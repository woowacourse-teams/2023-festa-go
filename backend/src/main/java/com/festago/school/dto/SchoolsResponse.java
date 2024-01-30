package com.festago.school.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.school.domain.School;
import java.util.List;

/**
 * @deprecated API 버저닝이 적용되면 해당 클래스 삭제
 */
@Deprecated(forRemoval = true)
public record SchoolsResponse(
    List<SchoolResponse> schools) {

    public static SchoolsResponse from(List<School> schools) {
        return schools.stream()
            .map(SchoolResponse::from)
            .collect(collectingAndThen(toList(), SchoolsResponse::new));
    }
}
