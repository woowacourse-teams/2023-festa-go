package com.festago.festago.presentation.model

data class TicketCodeUiModel(
    val code: String,
    val period: Int,
) {
    companion object {
        val EMPTY = TicketCodeUiModel(code = "code", period = 0)
    }
}
