package com.festago.student.infrastructure;

import com.festago.student.application.MailClient;
import java.util.function.Consumer;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev & !prod")
public class MockMailClient implements MailClient {

    @Override
    public void send(Consumer<SimpleMailMessage> mailMessageSupplier) {
        // no-op
    }
}
