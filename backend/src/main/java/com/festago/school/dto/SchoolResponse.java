package com.festago.school.dto;

import com.festago.school.domain.School;

/**
 * @deprecated API 버저닝이 적용되면 해당 클래스 삭제
 */
@Deprecated(forRemoval = true)
public record SchoolResponse(
    Long id,
    String domain,
    String name) {

    public static SchoolResponse from(School school) {
        return new SchoolResponse(
            school.getId(),
            school.getDomain(),
            school.getName()
        );
    }
}
