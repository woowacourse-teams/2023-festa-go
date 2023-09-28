package com.festago.staff.application;

import static java.util.stream.Collectors.joining;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.StaffVerificationCode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomStaffVerificationCodeProvider implements StaffVerificationCodeProvider {

    @Override
    public StaffVerificationCode provide(Festival festival) {
        String abbreviation = festival.getSchool().findAbbreviation();
        Random random = ThreadLocalRandom.current();
        String code = random.ints(0, 10)
            .mapToObj(String::valueOf)
            .limit(StaffVerificationCode.RANDOM_CODE_LENGTH)
            .collect(joining());
        return new StaffVerificationCode(abbreviation + code);
    }
}
