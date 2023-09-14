package com.festago.festago.model

class TextValidator private constructor(
    private val allowedChars: List<Char>,
    private val expectedLength: Int,
) {
    fun isValid(text: String): Boolean = isExpectedLength(text) && hasAllowedChars(text)

    private fun isExpectedLength(text: String) = text.length == expectedLength

    private fun hasAllowedChars(text: String) = text.toList().all { it in allowedChars }

    companion object {

        fun of(allowedCharSequence: String, expectedLength: Int): TextValidator {
            return TextValidator(allowedCharSequence.toList(), expectedLength)
        }
    }
}
