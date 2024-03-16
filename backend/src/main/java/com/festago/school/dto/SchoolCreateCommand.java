package com.festago.school.dto;

import com.festago.common.util.Validator;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import lombok.Builder;

@Builder
public record SchoolCreateCommand(
    String name,
    String domain,
    SchoolRegion region,
    String logoUrl,
    String backgroundImageUrl
) {

    public SchoolCreateCommand {
        Validator.notNull(name, "name");
        Validator.notNull(domain, "domain");
        Validator.notNull(region, "region");
    }

    /**
     * TODO 도메인에도 빌더 패턴을 적용해야할까?
     * 생성자에 같은 타입이 중복적으로 발생하여 버그 발생 가능성이 매우 높다.
     */
    public School toDomain() {
        return new School(
            null,
            domain,
            name,
            logoUrl,
            backgroundImageUrl,
            region
        );
    }
}
