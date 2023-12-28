package com.festago.fcm.application;

import com.festago.auth.application.AuthExtractor;
import com.festago.auth.domain.AuthPayload;
import com.festago.auth.dto.event.DeleteMemberEvent;
import com.festago.auth.dto.event.NewMemberEvent;
import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.repository.MemberFCMRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberRegisterEventListener {

    private final AuthExtractor authExtractor;
    private final MemberFCMRepository memberFCMRepository;

    @EventListener
    @Transactional
    public void registerNewMember(NewMemberEvent event) {
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

    @EventListener
    @Transactional
    public void deleteMember(DeleteMemberEvent event) {
        memberFCMRepository.deleteAllByMemberId(event.memberId());
    }
}
