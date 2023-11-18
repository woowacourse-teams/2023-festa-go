package com.festago.student.application;

import java.util.function.Consumer;
import org.springframework.mail.SimpleMailMessage;

public interface MailClient {

    void send(Consumer<SimpleMailMessage> mailMessageSupplier);
}
