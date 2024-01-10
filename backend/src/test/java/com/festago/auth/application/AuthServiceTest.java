package com.festago.auth.application;

import static com.festago.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemoryMemberRepository;
import com.festago.support.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthServiceTest {

    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    AuthService authService = new AuthService(memberRepository);

    @BeforeEach
    void setUp() {
        memberRepository.clear();
    }

    @Nested
    class 로그인 {

        @Test
        void 신규_회원으로_로그인하면_isNew가_참이다() {
            // given
            Member member = MemberFixture.member().build();
            UserInfo userInfo = new UserInfo(member.getSocialId(), member.getSocialType(), member.getNickname(),
                member.getProfileImage());

            // when
            LoginMemberDto response = authService.login(userInfo);

            // then
            assertThat(response.isNew())
                .isTrue();
        }

        @Test
        void 기존_회원으로_로그인하면_isNew가_거짓이다() {
            // given
            Member member = MemberFixture.member().build();
            memberRepository.save(member);
            UserInfo userInfo = new UserInfo(member.getSocialId(), member.getSocialType(), member.getNickname(),
                member.getProfileImage());

            // when
            LoginMemberDto response = authService.login(userInfo);

            // then
            assertThat(response.isNew())
                .isFalse();
        }
    }

    @Nested
    class 회원탈퇴 {

        @Test
        void 멤버가_없으면_예외() {
            // given
            Long memberId = 1L;

            // then
            assertThatThrownBy(() -> authService.deleteMember(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        void 성공() {
            // given
            Member member = MemberFixture.member().build();
            memberRepository.save(member);

            // when & then
            assertThatNoException()
                .isThrownBy(() -> authService.deleteMember(member.getId()));
        }
    }
}
