package com.festago.fcm.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFCMExpiredAtPolicyImpl implements MemberFCMExpiredAtPolicy {

    private static final int EXPIRED_DAY_OFFSET = 180;
    private final Clock clock;

    @Override
    public LocalDateTime provide() {
        return LocalDateTime.now(clock).plusDays(EXPIRED_DAY_OFFSET);
    }
}
