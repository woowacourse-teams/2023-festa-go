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

    // TODO @Slf4j 어노테이션 사용하면 어떨까요
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

    // TODO 사용하지 않는 메서드 같음
    // 비즈니스 파라미터의 isNewMember가 애매한 것 같음.
    // 다른 회원이 같은 FCM 토큰을 저장하면??
    // existsByFcmToken으로 Validation 고려해도 좋을듯 ex) 이미 사용중인 토큰입니다.
    @Async
    public void saveMemberFCM(boolean isNewMember, String accessToken, String fcmToken) {
        if (isNewMember) {
            saveNewMemberFCM(accessToken, fcmToken);
            return;
        }
        saveOriginMemberFCM(accessToken, fcmToken);
    }

    private void saveNewMemberFCM(String accessToken, String fcmToken) {
        Long memberId = extractMemberId(accessToken);
        memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
    }

    private void saveOriginMemberFCM(String accessToken, String fcmToken) {
        Long memberId = extractMemberId(accessToken);
        Optional<MemberFCM> memberFCM = memberFCMRepository.findByMemberIdAndFcmToken(memberId, fcmToken);
        if (memberFCM.isEmpty()) {
            memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
        }
    }

    private Long extractMemberId(String accessToken) {
        AuthPayload authPayload = authExtractor.extract(accessToken);
        return authPayload.getMemberId();
    }

    // TODO 사용하지 않는 메서드 같음
    @Async
    public void deleteMemberFCM(Long memberId) {
        memberFCMRepository.deleteAllByMemberId(memberId);
    }
}
