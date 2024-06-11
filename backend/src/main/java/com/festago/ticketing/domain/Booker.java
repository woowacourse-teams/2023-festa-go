package com.festago.ticketing.domain;

import com.festago.common.util.Validator;
import jakarta.annotation.Nullable;
import lombok.Builder;

/**
 * 티켓팅을 하는 사용자
 */
@Builder
public class Booker {

    private final Long memberId;
    private final Long schoolId;

    public Booker(Long memberId, Long schoolId) {
        Validator.notNull(memberId, "memberId");
        this.memberId = memberId;
        this.schoolId = schoolId;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Nullable
    public Long getSchoolId() {
        return schoolId;
    }
}
