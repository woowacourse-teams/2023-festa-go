package com.festago.fcm.domain;

import com.festago.fcm.repository.MemberFCMRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFCMRemoveOldTokensPolicy {

    private final MemberFCMRepository memberFCMRepository;

    public void delete(List<MemberFCM> memberFCMs) {
        memberFCMRepository.deleteByIn(memberFCMs);
    }
}
