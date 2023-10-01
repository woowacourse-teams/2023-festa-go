package com.festago.staff.application;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.StaffVerificationCode;

public interface StaffVerificationCodeProvider {

    StaffVerificationCode provide(Festival festival);
}
