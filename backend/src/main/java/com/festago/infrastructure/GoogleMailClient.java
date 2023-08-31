package com.festago.infrastructure;

import com.festago.domain.MailClient;
import com.festago.domain.VerificationMailPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Profile({"prod", "dev"})
public class GoogleMailClient implements MailClient {

    private final MailSender mailSender;
    private final String fromEmail;

    public GoogleMailClient(MailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    @Async
    public void send(VerificationMailPayload payload) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(payload.getUsername() + "@" + payload.getDomain());
        mail.setSubject("[페스타고] 학생 이메일 인증 코드");
        mail.setText("""
            페스타고 학생 이메일 인증 코드입니다.
            Code는 다음과 같습니다.
            %s
            """.formatted(payload.getCode()));
        mailSender.send(mail);
    }
}
