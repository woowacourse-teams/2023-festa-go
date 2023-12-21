package com.festago.student.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.ValidException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class VerificationCodeTest {

    @Test
    void null_이면_예외() {
        // when & then
        assertThatThrownBy(() -> new VerificationCode(null))
            .isInstanceOf(ValidException.class)
            .hasMessage("VerificationCode은/는 null 또는 공백이 될 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567"})
    void 길이가_6자리가_아니면_예외(String code) {
        // when & then
        assertThatThrownBy(() -> new VerificationCode(code))
            .isInstanceOf(ValidException.class)
            .hasMessage("VerificationCode의 길이는 6 이어야 합니다.");
    }

    @Test
    void 숫자가_아니면_예외() {
        // when & then
        assertThatThrownBy(() -> new VerificationCode("일이삼사오육"))
            .isInstanceOf(ValidException.class)
            .hasMessage("VerificationCode는 0~9의 양수 형식이어야 합니다.");
    }

    @Test
    void 음수이면_예외() {
        // when & then
        assertThatThrownBy(() -> new VerificationCode("-12345"))
            .isInstanceOf(ValidException.class)
            .hasMessage("VerificationCode는 0~9의 양수 형식이어야 합니다.");
    }

    @Test
    void 생성() {
        // when
        VerificationCode verificationCode = new VerificationCode("123456");

        // then
        assertThat(verificationCode.getValue()).isEqualTo("123456");
    }
}
