package com.festago.fcm.application;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.event.MemberRegisterEvent;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberRegisterEventListener {

    private final AuthExtractor authExtractor;
    private final MemberFCMRepository memberFCMRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    @Transactional
    public void registerNewMember(MemberRegisterEvent event) {
        if (event.isNew()) {
            saveNewMemberFCM(event.accessToken(), event.fcmToken());
            return;
        }
        saveOriginMemberFCM(event.accessToken(), event.fcmToken());
    }

    private void saveNewMemberFCM(String accessToken, String fcmToken) {
        Long memberId = extractMemberId(accessToken);
        memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
    }

    private Long extractMemberId(String accessToken) {
        AuthPayload authPayload = authExtractor.extract(accessToken);
        return authPayload.getMemberId();
    }

    private void saveOriginMemberFCM(String accessToken, String fcmToken) {
        Long memberId = extractMemberId(accessToken);
        Optional<MemberFCM> memberFCM = memberFCMRepository.findMemberFCMByMemberIdAndFcmToken(memberId, fcmToken);
        if (memberFCM.isEmpty()) {
            memberFCMRepository.save(new MemberFCM(memberId, fcmToken));
        }
    }
}
