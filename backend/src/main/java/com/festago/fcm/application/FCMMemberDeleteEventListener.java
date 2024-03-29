package com.festago.fcm.application;

import com.festago.auth.dto.event.MemberDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMMemberDeleteEventListener {

    private final MemberFCMService memberFCMService;

    @EventListener
    public void memberDeleteEventHandler(MemberDeletedEvent event) {
        memberFCMService.deleteAllMemberFCM(event.member().getId());
    }
}
