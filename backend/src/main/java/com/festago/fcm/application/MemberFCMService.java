package com.festago.fcm.application;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.MemberFCMsResponse;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberFCMService {

    private static final Logger log = LoggerFactory.getLogger(MemberFCMService.class);
    private static final int LEAST_MEMBER_FCM = 1;

    private final MemberFCMRepository memberFCMRepository;
    private final AuthExtractor authExtractor;

    public MemberFCMService(MemberFCMRepository memberFCMRepository, AuthExtractor authExtractor) {
        this.memberFCMRepository = memberFCMRepository;
        this.authExtractor = authExtractor;
    }

    @Transactional(readOnly = true)
    public MemberFCMsResponse findMemberFCM(Long memberId) {
        List<MemberFCM> memberFCM = memberFCMRepository.findByMemberId(memberId);
        if (memberFCM.size() < LEAST_MEMBER_FCM) {
            log.error("member {} 의 FCM 코드가 발급되지 않았습니다.", memberId);
            throw new InternalServerException(ErrorCode.FCM_NOT_FOUND);
        }
        return MemberFCMsResponse.from(memberFCM);
    }

    @Async
    public void saveMemberFCM(String accessToken, String fcmToken) {
        AuthPayload authPayload = authExtractor.extract(accessToken);
        Long memberId = authPayload.getMemberId();
        memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
    }
}
