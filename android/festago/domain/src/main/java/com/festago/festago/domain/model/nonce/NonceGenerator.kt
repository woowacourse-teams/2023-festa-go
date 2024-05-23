package com.festago.festago.domain.model.nonce

class NonceGenerator {
    fun generate() =
        List((MIN_LENGTH..MAX_LENGTH).random()) { (MIN_CHAR..MAX_CHAR).random() }.joinToString("")

    companion object {
        private const val MIN_LENGTH = 3
        private const val MAX_LENGTH = 6
        private const val MIN_CHAR = 'a'
        private const val MAX_CHAR = 'z'
    }
}
