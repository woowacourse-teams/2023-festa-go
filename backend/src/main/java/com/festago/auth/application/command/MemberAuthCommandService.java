package com.festago.auth.application.command;

import com.festago.auth.domain.RefreshToken;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.dto.event.MemberDeletedEvent;
import com.festago.auth.dto.v1.LoginResult;
import com.festago.auth.dto.v1.TokenResponse;
import com.festago.auth.repository.RefreshTokenRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberAuthCommandService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public LoginResult oauth2Login(UserInfo userInfo) {
        Member member = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .orElseGet(() -> signUp(userInfo));
        refreshTokenRepository.deleteByMemberId(member.getId());
        RefreshToken refreshToken = saveRefreshToken(member.getId());
        return new LoginResult(member.getId(), member.getNickname(), refreshToken.getId(), refreshToken.getExpiredAt());
    }

    private Member signUp(UserInfo userInfo) {
        return memberRepository.save(userInfo.toMember());
    }

    private RefreshToken saveRefreshToken(Long memberId) {
        return refreshTokenRepository.save(RefreshToken.of(memberId, LocalDateTime.now(clock)));
    }

    public void logout(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

    public TokenResponse refresh(Long memberId, UUID oldFreshToken) {
        refreshTokenRepository.findById(oldFreshToken).ifPresentOrElse(
            refreshToken -> {
                refreshTokenRepository.deleteByMemberId(memberId);
            },
            () -> {
                // 회원이 해커에게 정보가 털린 경우
                throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
        );
        RefreshToken newRefreshToken = saveRefreshToken(memberId);
        return new TokenResponse(
            newRefreshToken.getId().toString(),
            newRefreshToken.getExpiredAt()
        );
    }

    public void deleteAccount(Long memberId) {
        Member member = memberRepository.getOrThrow(memberId);
        logDeleteMember(member);
        memberRepository.delete(member);
        eventPublisher.publishEvent(new MemberDeletedEvent(member));
    }

    private void logDeleteMember(Member member) {
        log.info("[DELETE MEMBER] memberId: {} / socialType: {} / socialId: {}",
            member.getId(), member.getSocialType(), member.getSocialId());
    }
}
