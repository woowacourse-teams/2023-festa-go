package com.festago.festago.model

class SentenceValidation(private val digits: List<String>, private val length: Int) {

    fun isValidate(sentence: String): Boolean = validateDigits(sentence) && validateLength(sentence)

    private fun validateLength(code: String) = code.length == length

    private fun validateDigits(code: String) = code.chunked(CHUNKED_SIZE).all { it in digits }

    companion object {
        private const val CHUNKED_SIZE = 1

        fun of(digits: String, length: Int): SentenceValidation {
            return SentenceValidation(digits.chunked(CHUNKED_SIZE), length)
        }
    }
}
