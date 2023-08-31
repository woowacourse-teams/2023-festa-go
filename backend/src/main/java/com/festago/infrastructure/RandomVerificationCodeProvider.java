package com.festago.infrastructure;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

import com.festago.domain.VerificationCode;
import com.festago.domain.VerificationCodeProvider;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomVerificationCodeProvider implements VerificationCodeProvider {

    private static final int CODE_LENGTH = 6;

    @Override
    public VerificationCode provide() {
        Random random = ThreadLocalRandom.current();
        return random.ints(0, 10)
            .mapToObj(String::valueOf)
            .limit(CODE_LENGTH)
            .collect(collectingAndThen(joining(), VerificationCode::new));
    }
}
