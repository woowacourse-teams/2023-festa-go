package com.festago.student.application;

import com.festago.student.domain.VerificationCode;

public interface VerificationCodeProvider {

    VerificationCode provide();
}
