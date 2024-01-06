package com.festago.fcm.application;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.event.MemberRegisterEvent;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
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
        Long memberId = extractMemberId(event.accessToken());
        String fcmToken = event.fcmToken();
        memberFCMRepository.findByMemberIdAndFcmToken(memberId, fcmToken)
            .ifPresentOrElse(
                ignore -> {
                },
                () -> memberFCMRepository.save(new MemberFCM(memberId, fcmToken))
            );
    }

    private Long extractMemberId(String accessToken) {
        AuthPayload authPayload = authExtractor.extract(accessToken);
        return authPayload.getMemberId();
    }
}
