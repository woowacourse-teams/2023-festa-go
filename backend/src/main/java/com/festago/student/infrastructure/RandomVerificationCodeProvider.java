package com.festago.student.infrastructure;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

import com.festago.student.application.VerificationCodeProvider;
import com.festago.student.domain.VerificationCode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomVerificationCodeProvider implements VerificationCodeProvider {

    @Override
    public VerificationCode provide() {
        Random random = ThreadLocalRandom.current();
        return random.ints(0, 10)
            .mapToObj(String::valueOf)
            .limit(VerificationCode.LENGTH)
            .collect(collectingAndThen(joining(), VerificationCode::new));
    }
}
