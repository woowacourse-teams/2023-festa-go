package com.festago.fcm.application;

import com.festago.auth.dto.event.MemberDeletedEvent;
import com.festago.fcm.application.command.MemberFCMCommandService;
import com.festago.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMMemberDeleteEventListener {

    private final MemberFCMCommandService memberFCMService;

    @EventListener
    public void memberDeleteEventHandler(MemberDeletedEvent event) {
        Member member = event.member();
        memberFCMService.deleteAllMemberFCM(member.getId());
    }
}
