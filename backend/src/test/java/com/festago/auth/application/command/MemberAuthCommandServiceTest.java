package com.festago.auth.application.command;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.spy;

import com.festago.auth.domain.RefreshToken;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.domain.UserInfoMemberMapper;
import com.festago.auth.repository.MemoryRefreshTokenRepository;
import com.festago.auth.repository.RefreshTokenRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.common.exception.UnauthorizedException;
import com.festago.member.domain.DefaultNicknamePolicy;
import com.festago.member.domain.Member;
import com.festago.member.domain.SocialType;
import com.festago.member.repository.MemberRepository;
import com.festago.member.repository.MemoryMemberRepository;
import com.festago.support.fixture.MemberFixture;
import com.festago.support.fixture.RefreshTokenFixture;
import java.time.Clock;
import java.time.LocalDateTime;
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
        DefaultNicknamePolicy defaultNicknamePolicy = () -> "nickname";
        memberAuthCommandService = new MemberAuthCommandService(
            memberRepository,
            refreshTokenRepository,
            mock(ApplicationEventPublisher.class),
            new UserInfoMemberMapper(defaultNicknamePolicy),
            clock
        );
    }

    @Nested
    class login {

        @Test
        void 신규_회원으로_로그인하면_회원과_리프래쉬_토큰이_저장된다() {
            // when
            var actual = memberAuthCommandService.login(getUserInfo("1"));

            // then
            assertThat(memberRepository.findById(actual.memberId())).isPresent();
            assertThat(refreshTokenRepository.findById(actual.refreshToken())).isPresent();
        }

        @Test
        void 기존_회원으로_로그인해도_기존_리프레쉬_토큰이_삭제되지_않는다() {
            // given
            Member member = memberRepository.save(MemberFixture.builder().build());
            RefreshToken originToken = refreshTokenRepository.save(
                RefreshTokenFixture.builder().memberId(member.getId()).build());

            // when
            var actual = memberAuthCommandService.login(getUserInfo(member.getSocialId()));

            // then
            assertThat(refreshTokenRepository.findById(originToken.getId())).isPresent();
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
            memberAuthCommandService.logout(member.getId(), originToken.getId());

            // then
            assertThat(refreshTokenRepository.findById(originToken.getId())).isEmpty();
        }

        @Test
        void 다른_회원의_리프래쉬_토큰으로_로그아웃하면_해당_리프래쉬_토큰은_삭제되지_않는다() {
            // given
            Member 회원A = memberRepository.save(MemberFixture.builder().build());
            Member 회원B = memberRepository.save(MemberFixture.builder().build());
            RefreshToken 회원A_리프래쉬_토큰 = refreshTokenRepository.save(
                RefreshTokenFixture.builder().memberId(회원A.getId()).build());

            // when
            memberAuthCommandService.logout(회원B.getId(), 회원A_리프래쉬_토큰.getId());

            // then
            assertThat(refreshTokenRepository.findById(회원A_리프래쉬_토큰.getId())).isPresent();
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
            var actual = memberAuthCommandService.refresh(originToken.getId());

            // then
            assertThat(refreshTokenRepository.findById(originToken.getId())).isEmpty();
            assertThat(refreshTokenRepository.findById(fromString(actual.token()))).isPresent();
        }

        @Test
        void 기존_리프래쉬_토큰이_없으면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> memberAuthCommandService.refresh(UUID.randomUUID()))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
        }

        @Test
        void 리프래쉬를_요청한_리프래쉬_토큰이_만료되면_예외가_발생한다() {
            // given
            Member member = memberRepository.save(MemberFixture.builder().build());
            LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
            RefreshToken expiredToken = refreshTokenRepository.save(
                RefreshTokenFixture.builder().memberId(member.getId()).expiredAt(yesterday).build());

            // when & then
            assertThatThrownBy(() -> memberAuthCommandService.refresh(expiredToken.getId()))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(ErrorCode.EXPIRED_REFRESH_TOKEN.getMessage());
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

    private UserInfo getUserInfo(String socialId) {
        return new UserInfo(
            socialId,
            SocialType.FESTAGO,
            "오리",
            "https://image.com/image.png"
        );
    }
}
