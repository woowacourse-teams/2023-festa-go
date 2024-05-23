package com.festago.auth.application.command;

import com.festago.auth.domain.RefreshToken;
import com.festago.auth.domain.UserInfo;
import com.festago.auth.domain.UserInfoMemberMapper;
import com.festago.auth.dto.event.MemberDeletedEvent;
import com.festago.auth.dto.v1.LoginResult;
import com.festago.auth.dto.v1.TokenRefreshResult;
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
    private final UserInfoMemberMapper userInfoMemberMapper;
    private final Clock clock;

    public LoginResult login(UserInfo userInfo) {
        Member member = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
            .orElseGet(() -> signUp(userInfo));
        RefreshToken refreshToken = saveRefreshToken(member.getId());
        return new LoginResult(
            member.getId(),
            member.getNickname(),
            member.getProfileImage(),
            refreshToken.getId(),
            refreshToken.getExpiredAt()
        );
    }

    private Member signUp(UserInfo userInfo) {
        Member member = userInfoMemberMapper.toMember(userInfo);
        return memberRepository.save(member);
    }

    private RefreshToken saveRefreshToken(Long memberId) {
        return refreshTokenRepository.save(RefreshToken.of(memberId, LocalDateTime.now(clock)));
    }

    public void logout(Long memberId, UUID refreshTokenId) {
        refreshTokenRepository.findById(refreshTokenId)
            .ifPresent(refreshToken -> {
                if (refreshToken.isOwner(memberId)) {
                    refreshTokenRepository.deleteById(refreshTokenId);
                }
            });
    }

    public TokenRefreshResult refresh(UUID refreshTokenId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> {
                log.warn("탈취 가능성이 있는 리프래쉬 토큰이 있습니다. token={}", refreshTokenId);
                return new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
            });
        if (refreshToken.isExpired(LocalDateTime.now(clock))) {
            log.info("만료된 리프래쉬 토큰이 있습니다. memberId={}, token={}", refreshToken.getMemberId(), refreshTokenId);
            throw new UnauthorizedException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
        refreshTokenRepository.deleteById(refreshTokenId);
        RefreshToken newRefreshToken = saveRefreshToken(refreshToken.getMemberId());
        return new TokenRefreshResult(
            newRefreshToken.getMemberId(),
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
