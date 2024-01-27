package com.festago.festago.model

sealed class ErrorCode : Throwable() {

    class NEED_STUDENT_VERIFICATION : ErrorCode()
    class RESERVE_TICKET_OVER_AMOUNT : ErrorCode()
    class TICKET_SOLD_OUT : ErrorCode()
    class UNKNOWN : ErrorCode()
}
