package com.festago.fcm.application;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberFCMService {
    
    private final MemberFCMRepository memberFCMRepository;
    private final AuthExtractor authExtractor;

    public MemberFCMService(MemberFCMRepository memberFCMRepository, AuthExtractor authExtractor) {
        this.memberFCMRepository = memberFCMRepository;
        this.authExtractor = authExtractor;
    }

    @Transactional(readOnly = true)
    public List<String> findMemberFCMTokens(Long memberId) {
        return memberFCMRepository.findAllTokenByMemberId(memberId);
    }

    @Async
    public void saveMemberFCM(String accessToken, String fcmToken) {
        AuthPayload authPayload = authExtractor.extract(accessToken);
        Long memberId = authPayload.getMemberId();
        memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
    }
}
