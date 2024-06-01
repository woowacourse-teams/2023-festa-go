package com.festago.fcm.domain;

import java.time.LocalDateTime;

public interface MemberFCMExpiredAtPolicy {

    LocalDateTime provide();
}
