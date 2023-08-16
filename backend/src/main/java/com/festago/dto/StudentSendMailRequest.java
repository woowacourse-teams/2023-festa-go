package com.festago.dto;

public record StudentSendMailRequest(
    String username,
    Long schoolId
) {

}
