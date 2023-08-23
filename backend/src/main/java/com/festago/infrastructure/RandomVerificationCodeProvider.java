package com.festago.infrastructure;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

import com.festago.domain.VerificationCode;
import com.festago.domain.VerificationCodeProvider;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class RandomVerificationCodeProvider implements VerificationCodeProvider {

    private static final int CODE_LENGTH = 6;

    @Override
    public VerificationCode provide() {
        Random random = ThreadLocalRandom.current();
        return IntStream.range(0, CODE_LENGTH)
            .mapToObj(i -> String.valueOf(random.nextInt(10)))
            .collect(collectingAndThen(joining(), VerificationCode::new));
    }
}
