package com.festago.staff.domain;

import static com.festago.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StaffVerificationCodeTest {

    @Test
    void null이면_예외() {
        // when & then
        assertThatThrownBy(() -> new StaffVerificationCode(null))
            .isInstanceOf(InternalServerException.class)
            .hasMessage(INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void 생성() {
        // when & then
        assertThatNoException()
            .isThrownBy(() -> new StaffVerificationCode("festa1234"));
    }
}
