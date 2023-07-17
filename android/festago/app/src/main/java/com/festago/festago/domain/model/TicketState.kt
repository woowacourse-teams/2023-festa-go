package com.festago.festago.domain.model

enum class TicketState(val state: String) {
    BEFORE_ENTRY("BEFORE_ENTRY"),
    AFTER_ENTRY("AFTER_ENTRY"),
    AWAY("AWAY"),
    ;

    companion object {
        fun of(state: String): TicketState {
            return when (state) {
                "BEFORE_ENTRY" -> BEFORE_ENTRY
                "AFTER_ENTRY" -> AFTER_ENTRY
                "AWAY" -> AWAY
                else -> throw IllegalArgumentException("Unknown ticket state: $state")
            }
        }
    }
}
