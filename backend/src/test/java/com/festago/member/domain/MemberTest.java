package com.festago.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.auth.domain.SocialType;
import com.festago.common.exception.ValidException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @Test
    void Member_생성_성공() {
        // given
        Member member = new Member(1L, "12345", SocialType.FESTAGO, "nickname", "profileImage.png");

        // when & then
        assertThat(member.getId()).isEqualTo(1L);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void socialId가_null_또는_공백이면_예외(String socialId) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, socialId, SocialType.FESTAGO, "nickname", "profileImage.png"))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void socialId의_길이가_255자를_초과하면_예외() {
        // given
        String socialId = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Member(1L, socialId, SocialType.FESTAGO, "nickname", "profileImage.png"))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    void socialType이_null이면_예외(SocialType socialType) {
        // when & then
        assertThatThrownBy(() -> new Member(1L, "12345", socialType, "nickname", "profileImage.png"))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void nickname이_null_또는_공백이면_기본_닉네임_생성(String nickname) {
        // given && when
        Member member = new Member(1L, "12345", SocialType.FESTAGO, nickname, "profileImage.png");

        // then
        assertThat(member.getNickname()).isEqualTo("FestivalLover");
    }

    @Test
    void nickname의_길이가_30자를_초과하면_예외() {
        // given
        String nickname = "1".repeat(31);

        // when & then
        assertThatThrownBy(() -> new Member(1L, "12345", SocialType.FESTAGO, nickname, "profileImage.png"))
            .isInstanceOf(ValidException.class);
    }

    @Test
    void profileImage의_길이가_255자를_초과하면_예외() {
        // given
        String profileImage = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Member(1L, "12345", SocialType.FESTAGO, "nickname", profileImage))
            .isInstanceOf(ValidException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void profileImage가_null_또는_공백이면_기본값이_할당된다(String profileImage) {
        // given
        Member actual = new Member("12345", SocialType.FESTAGO, "nickname", profileImage);

        // when & then
        assertThat(actual.getProfileImage()).isEmpty();
    }
}
