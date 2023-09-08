package com.festago.festago.model

@JvmInline
value class StudentVerificationCode(val value: String) {

    init {
        require(
            TextValidator(DIGITS.toList(), CODE_LENGTH).isValid(value),
        ) { ERROR_CODE_VALIDATION }
    }

    companion object {
        private const val ERROR_CODE_VALIDATION = "[ERROR]: StudentVerificationCode Validation"
        private const val CODE_LENGTH = 6
        private const val DIGITS = "0123456789"
    }
}
