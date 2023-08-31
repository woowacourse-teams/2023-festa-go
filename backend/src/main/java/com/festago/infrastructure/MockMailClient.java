package com.festago.infrastructure;

import com.festago.domain.MailClient;
import com.festago.domain.VerificationMailPayload;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "test"})
public class MockMailClient implements MailClient {

    @Override
    public void send(VerificationMailPayload payload) {
    }
}
