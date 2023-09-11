package com.festago.domain;

public interface MailClient {

    void send(VerificationMailPayload payload);
}
