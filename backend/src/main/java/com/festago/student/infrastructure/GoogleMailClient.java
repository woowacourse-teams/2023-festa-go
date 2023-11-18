package com.festago.student.infrastructure;

import com.festago.student.application.MailClient;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Profile("prod | dev")
public class GoogleMailClient implements MailClient {

    private final MailSender mailSender;
    private final String fromEmail;

    public GoogleMailClient(MailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    @Async
    public void send(Consumer<SimpleMailMessage> mailMessageConsumer) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessageConsumer.accept(mailMessage);
        mailMessage.setFrom(fromEmail);
        mailSender.send(mailMessage);
    }
}
