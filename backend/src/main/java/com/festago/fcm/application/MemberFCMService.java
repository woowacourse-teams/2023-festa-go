package com.festago.fcm.application;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.MemberFCMsResponse;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile({"prod", "dev"})
public class MemberFCMService {

    private static final Logger logger = LoggerFactory.getLogger(MemberFCMService.class);
    private static final int LEAST_MEMBER_FCM = 1;

    private final MemberFCMRepository memberFCMRepository;

    public MemberFCMService(MemberFCMRepository memberFCMRepository) {
        this.memberFCMRepository = memberFCMRepository;
    }

    @Transactional(readOnly = true)
    public MemberFCMsResponse findMemberFCM(Long memberId) {
        List<MemberFCM> memberFCM = memberFCMRepository.findByMemberId(memberId);
        if (memberFCM.size() < LEAST_MEMBER_FCM) {
            logger.error("member {} 의 FCM 코드가 발급되지 않았습니다.", memberId);
            throw new InternalServerException(ErrorCode.FCM_NOT_FOUND);
        }
        return MemberFCMsResponse.from(memberFCM);
    }
}
