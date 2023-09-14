package com.festago.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.FestaGoException;
import com.festago.common.exception.InternalServerException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FestaGoExceptionTest {

    @Test
    void cause_예외가_있을때_getCause를_호출하면_null이_아니다() {
        // given
        FestaGoException exception = new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR,
            new IllegalArgumentException("예외"));

        // when & then
        assertThat(exception.getCause())
            .isNotNull();
    }

    @Test
    void casue_예외가_없을때_getCause를_호출하면_null이다() {
        // given
        FestaGoException exception = new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);

        // when & then
        assertThat(exception.getCause())
            .isNull();
    }

    @Test
    void cause_예외가_있을때_getRootCause를_호출하면_null이_아니다() {
        // given
        FestaGoException exception = new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR,
            new IllegalArgumentException("예외"));

        // when & then
        assertThat(exception.getRootCause())
            .isNotNull();
    }

    @Test
    void casue_예외가_없을때_getRootCause를_호출하면_null이다() {
        // given
        FestaGoException exception = new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);

        // when & then
        assertThat(exception.getRootCause())
            .isNull();
    }

    @Test
    void cause_예외가_있을때_getMostSpecificCause를_호출하면_null이_아니다() {
        // given
        FestaGoException exception = new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR,
            new IllegalArgumentException("예외"));

        // when & then
        assertThat(exception.getMostSpecificCause())
            .isNotNull();
    }

    @Test
    void casue_예외가_없을때_getMostSpecificCause를_호출하면_null이_아니다() {
        // given
        FestaGoException exception = new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);

        // when & then
        assertThat(exception.getMostSpecificCause())
            .isNotNull();
    }
}
