package com.festago.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.LoginMemberDto;
import com.festago.auth.dto.event.MemberDeleteEvent;
import com.festago.auth.dto.event.MemberLoginEvent;
import com.festago.common.exception.NotFoundException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.support.MemberFixture;
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
    ApplicationEventPublisher publisher;

    @InjectMocks
    AuthService authService;

    @Nested
    class 로그인 {

        @Test
        void 신규_회원으로_로그인() {
            // given
            Member member = MemberFixture.member()
                .id(1L)
                .build();
            given(memberRepository.findBySocialIdAndSocialType(anyString(), any(SocialType.class)))
                .willReturn(Optional.empty());
            given(memberRepository.save(any(Member.class)))
                .willReturn(member);
            UserInfo userInfo = new UserInfo(member.getSocialId(), member.getSocialType(), member.getNickname(),
                member.getProfileImage());

            // when
            LoginMemberDto response = authService.login(userInfo, "fcmToken");

            // then
            assertThat(response.isNew())
                .isTrue();
            verify(publisher, times(1)).publishEvent(any(MemberLoginEvent.class));
        }

        @Test
        void 기존_회원으로_로그인() {
            // given
            Member member = MemberFixture.member()
                .id(1L)
                .build();
            given(memberRepository.findBySocialIdAndSocialType(anyString(), any(SocialType.class)))
                .willReturn(Optional.of(member));
            UserInfo userInfo = new UserInfo(member.getSocialId(), member.getSocialType(), member.getNickname(),
                member.getProfileImage());

            // when
            LoginMemberDto response = authService.login(userInfo, "fcmToken");

            // then
            assertThat(response.isNew())
                .isFalse();
            verify(publisher, times(1)).publishEvent(any(MemberLoginEvent.class));
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
                .hasMessage("존재하지 않는 멤버입니다.");
        }

        @Test
        void 성공() {
            // given
            Long memberId = 1L;
            Member member = MemberFixture.member().id(memberId).build();
            given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

            // when & then
            assertThatNoException()
                .isThrownBy(() -> authService.deleteMember(memberId));
            verify(publisher, times(1)).publishEvent(any(MemberDeleteEvent.class));
        }
    }
}
