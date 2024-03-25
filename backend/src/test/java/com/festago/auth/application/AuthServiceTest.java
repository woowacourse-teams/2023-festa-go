package com.festago.auth.application;

import static com.festago.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.support.fixture.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    AuthService authService;

    @Nested
    class 로그인 {

        @Test
        void 신규_회원으로_로그인() {
            // given
            Member member = MemberFixture.builder()
                .id(1L)
                .build();
            given(memberRepository.findBySocialIdAndSocialType(anyString(), any(SocialType.class)))
                .willReturn(Optional.empty());
            given(memberRepository.save(any(Member.class)))
                .willReturn(member);
            UserInfo userInfo = new UserInfo(member.getSocialId(), member.getSocialType(), member.getNickname(),
                member.getProfileImage());

            // when
            LoginMemberDto response = authService.login(userInfo);

            // then
            assertThat(response.isNew())
                .isTrue();
        }

        @Test
        void 기존_회원으로_로그인() {
            // given
            Member member = MemberFixture.builder()
                .id(1L)
                .build();
            given(memberRepository.findBySocialIdAndSocialType(anyString(), any(SocialType.class)))
                .willReturn(Optional.of(member));
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
            given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> authService.deleteMember(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            Member member = MemberFixture.builder().id(memberId).build();
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

            // when & then
            assertThatNoException()
                .isThrownBy(() -> authService.deleteMember(memberId));
        }
    }
}
