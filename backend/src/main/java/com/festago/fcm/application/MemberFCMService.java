package com.festago.fcm.application;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.MemberFCMsResponse;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberFCMService {

    private static final Logger log = LoggerFactory.getLogger(MemberFCMService.class);

    private final MemberFCMRepository memberFCMRepository;
    private final AuthExtractor authExtractor;

    public MemberFCMService(MemberFCMRepository memberFCMRepository, AuthExtractor authExtractor) {
        this.memberFCMRepository = memberFCMRepository;
        this.authExtractor = authExtractor;
    }

    @Transactional(readOnly = true)
    public MemberFCMsResponse findMemberFCM(Long memberId) {
        List<MemberFCM> memberFCM = memberFCMRepository.findByMemberId(memberId);
        if (memberFCM.isEmpty()) {
            log.warn("member {} 의 FCM 토큰이 발급되지 않았습니다.", memberId);
        }
        return MemberFCMsResponse.from(memberFCM);
    }

    @Async
    public void saveOriginMemberFCM(String accessToken, String fcmToken) {
        Long memberId = extractMemberId(accessToken);
        Optional<MemberFCM> memberFCM = memberFCMRepository.findMemberFCMByMemberIdAndFcmToken(memberId, fcmToken);
        if (memberFCM.isPresent()) {
            return;
        }
        memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
    }

    private Long extractMemberId(String accessToken) {
        AuthPayload authPayload = authExtractor.extract(accessToken);
        return authPayload.getMemberId();
    }

    @Async
    public void saveNewMemberFCM(String accessToken, String fcmToken) {
        Long memberId = extractMemberId(accessToken);
        memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
    }

    @Async
    public void deleteMemberFCM(Long memberId) {
        memberFCMRepository.deleteAllByMemberId(memberId);
    }
}
