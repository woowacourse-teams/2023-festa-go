package com.festago.student.infrastructure;

import com.festago.student.application.MailClient;
import com.festago.student.domain.VerificationMailPayload;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev & !prod")
public class MockMailClient implements MailClient {

    @Override
    public void send(VerificationMailPayload payload) {
        // no-op
    }
}
