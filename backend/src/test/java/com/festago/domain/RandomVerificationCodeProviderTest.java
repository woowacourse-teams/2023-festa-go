package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.infrastructure.RandomVerificationCodeProvider;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RandomVerificationCodeProviderTest {

    RandomVerificationCodeProvider codeProvider = new RandomVerificationCodeProvider();

    @Test
    void 생성() {
        // when
        VerificationCode code = codeProvider.provide();

        // then
        assertThat(code.getValue()).hasSize(6);
    }
}
