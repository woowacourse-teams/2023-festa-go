package com.festago.festago.model

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class StudentVerificationCodeTest {

    @ParameterizedTest
    @ValueSource(strings = ["000000", "123456", "664325", "194823", "999999"])
    fun `학생 인증 코드는 6자리 숫자여야 한다`(code: String) {
        assertDoesNotThrow { StudentVerificationCode(code) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["1234567", "11", "45567", "0"])
    fun `학생 인증 코드가 6자리가 아니면 예외가 발생한다 `(code: String) {
        assertThrows<IllegalArgumentException> { StudentVerificationCode(code) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["123456a", "a123456", "asdfeq", "999999a"])
    fun `학생 인증 코드에 숫자가 아닌 문자가 포함되면 예외가 발생한다 `(code: String) {
        assertThrows<IllegalArgumentException> { StudentVerificationCode(code) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["12345&", "a1#345", "******"])
    fun `학생 인증 코드가 특수 문자가 포함되면 예외가 발생한다 `(code: String) {
        assertThrows<IllegalArgumentException> { StudentVerificationCode(code) }
    }
}
