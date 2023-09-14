package com.festago.student.dto;

public record StudentSendMailRequest(
    String username,
    Long schoolId
) {

}
