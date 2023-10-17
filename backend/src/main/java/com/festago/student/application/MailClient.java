package com.festago.student.application;

import com.festago.student.domain.VerificationMailPayload;

public interface MailClient {

    void send(VerificationMailPayload payload);
}
