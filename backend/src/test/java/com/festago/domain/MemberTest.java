package com.festago.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.support.MemberFixture;
import com.festago.zmember.domain.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @Test
    void 프로필_이미지가_null이면_기본값이_할당된다() {
        // when
        Member actual = MemberFixture.member()
            .profileImage(null)
            .build();

        // then
        assertThat(actual.getProfileImage()).isNotNull();
    }
}
