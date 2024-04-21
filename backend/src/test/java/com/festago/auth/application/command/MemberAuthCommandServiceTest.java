package com.festago.auth.application.command;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.spy;

import com.festago.auth.domain.RefreshToken;
import com.festago.auth.domain.SocialType;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.v1.TokenResponse;
import com.festago.auth.repository.MemoryRefreshTokenRepository;
import com.festago.auth.repository.RefreshTokenRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.exception.UnauthorizedException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.member.repository.MemoryMemberRepository;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.RefreshTokenFixture;
import java.time.Clock;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberAuthCommandServiceTest {

    MemberAuthCommandService memberAuthCommandService;

    MemberRepository memberRepository;

    RefreshTokenRepository refreshTokenRepository;

    Clock clock;

    @BeforeEach
    void setUp() {
        clock = spy(Clock.systemDefaultZone());
        memberRepository = new MemoryMemberRepository();
        refreshTokenRepository = new MemoryRefreshTokenRepository();
        memberAuthCommandService = new MemberAuthCommandService(
            memberRepository,
            refreshTokenRepository,
            mock(ApplicationEventPublisher.class),
            clock
        );
    }

    @Nested
    class oauth2Login {

        @Test
        void 신규_회원으로_로그인하면_회원과_리프래쉬_토큰이_저장된다() {
            // when
            var actual = memberAuthCommandService.oauth2Login(getUserInfo());

            // then
            assertThat(memberRepository.findById(actual.memberId())).isPresent();
            assertThat(refreshTokenRepository.findById(actual.refreshToken())).isPresent();
        }

        @Test
        void 기존_회원으로_로그인하면_기존_리프레쉬_토큰이_삭제된다() {
            // given
            Member member = memberRepository.save(MemberFixture.builder().build());
            RefreshToken originToken = refreshTokenRepository.save(
                RefreshTokenFixture.builder().memberId(member.getId()).build());

            // when
            var actual = memberAuthCommandService.oauth2Login(getUserInfo());

            // then
            assertThat(refreshTokenRepository.findById(originToken.getId())).isEmpty();
            assertThat(refreshTokenRepository.findById(actual.refreshToken())).isPresent();
        }
    }

    @Nested
    class logout {

        @Test
        void 회원의_리프래쉬_토큰이_삭제된다() {
            // given
            Member member = memberRepository.save(MemberFixture.builder().build());
            RefreshToken originToken = refreshTokenRepository.save(
                RefreshTokenFixture.builder().memberId(member.getId()).build());

            // when
            memberAuthCommandService.logout(member.getId());

            // then
            assertThat(refreshTokenRepository.findById(originToken.getId())).isEmpty();
        }
    }

    @Nested
    class refresh {

        @Test
        void 기존_리프래쉬_토큰이_있으면_기존_리프래쉬_토큰을_삭제하고_새로운_토큰을_저장한다() {
            // given
            Member member = memberRepository.save(MemberFixture.builder().build());
            RefreshToken originToken = refreshTokenRepository.save(
                RefreshTokenFixture.builder().memberId(member.getId()).build());

            // when
            TokenResponse actual = memberAuthCommandService.refresh(member.getId(), originToken.getId());

            // then
            assertThat(refreshTokenRepository.findById(originToken.getId())).isEmpty();
            assertThat(refreshTokenRepository.findById(fromString(actual.token()))).isPresent();
        }

        @Test
        void 기존_리프래쉬_토큰이_없으면_예외가_발생한다() {
            // given
            Member member = memberRepository.save(MemberFixture.builder().build());

            // when & then
            assertThatThrownBy(() -> memberAuthCommandService.refresh(member.getId(), UUID.randomUUID()))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
        }
    }

    @Nested
    class deleteAccount {

        @Test
        void 해당_회원이_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> memberAuthCommandService.deleteAccount(4885L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }

        @Test
        void 해당_회원이_있으면_회원이_삭제된다() {
            // given
            Member member = memberRepository.save(MemberFixture.builder().build());

            // when
            memberAuthCommandService.deleteAccount(member.getId());

            // then
            assertThat(memberRepository.findById(member.getId())).isEmpty();
        }
    }

    private UserInfo getUserInfo() {
        return new UserInfo(
            "1",
            SocialType.FESTAGO,
            "오리",
            "https://image.com/image.png"
        );
    }
}
