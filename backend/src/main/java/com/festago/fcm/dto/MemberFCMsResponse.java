package com.festago.fcm.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.festago.fcm.domain.MemberFCM;
import java.util.List;

public record MemberFCMsResponse(List<MemberFCMResponse> memberFCMs
) {

    public static MemberFCMsResponse from(List<MemberFCM> memberFCMs) {
        return memberFCMs.stream()
            .map(MemberFCMResponse::from)
            .collect(collectingAndThen(toList(), MemberFCMsResponse::new));
    }
}
