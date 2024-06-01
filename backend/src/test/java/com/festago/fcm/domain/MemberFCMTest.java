package com.festago.fcm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.ValidException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberFCMTest {

    @Test
    void MemberFCM_생성_성공() {
        // given
        MemberFCM memberFCM = new MemberFCM(1L, "token", LocalDateTime.now());

        // when & then
        assertThat(memberFCM.getMemberId()).isEqualTo(1L);
        assertThat(memberFCM.getFcmToken()).isEqualTo("token");
    }

    @ParameterizedTest
    @NullSource
    void memberId가_null이면_예외(Long memberId) {
        // when & then
        assertThatThrownBy(() -> new MemberFCM(memberId, "token", LocalDateTime.now()))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void token이_null_또는_공백이면_예외(String token) {
        // when & then
        assertThatThrownBy(() -> new MemberFCM(1L, token, LocalDateTime.now()))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void token의_길이가_255자를_초과하면_예외() {
        // given
        String token = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new MemberFCM(1L, token, LocalDateTime.now()))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    void expiredAt이_null이면_예외(LocalDateTime expiredAt) {
        // when & then
        assertThatThrownBy(() -> new MemberFCM(1L, "token", expiredAt))
            .isInstanceOf(ValidException.class);
    }
}
