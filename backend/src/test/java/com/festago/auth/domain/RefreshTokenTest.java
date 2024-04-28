package com.festago.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RefreshTokenTest {

    @Nested
    class isExpired {

        @Test
        void 주어진_시간이_만료시간보다_이후이면_참() {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T18:00:00");
            LocalDateTime future = now.plusSeconds(1);
            RefreshToken refreshToken = new RefreshToken(1L, now);

            // when
            boolean actual = refreshToken.isExpired(future);

            // then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1})
        void 주어진_시간이_만료시간보다_같거나_이전이면_거짓(long second) {
            // given
            LocalDateTime now = LocalDateTime.parse("2077-06-30T18:00:00");
            LocalDateTime past = now.minusSeconds(second);
            RefreshToken refreshToken = new RefreshToken(1L, now);

            // when
            boolean actual = refreshToken.isExpired(past);

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class isOwner {

        @Test
        void 주어진_식별자가_자신의_memberId와_같으면_참() {
            // given
            Long memberId = 1L;
            RefreshToken refreshToken = new RefreshToken(memberId, LocalDateTime.now());

            // when
            boolean actual = refreshToken.isOwner(memberId);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주어진_식별자가_자신의_memberId와_다르면_거짓() {
            // given
            Long memberId = 1L;
            Long otherId = 2L;
            RefreshToken refreshToken = new RefreshToken(memberId, LocalDateTime.now());

            // when
            boolean actual = refreshToken.isOwner(otherId);

            // then
            assertThat(actual).isFalse();
        }
    }
}
