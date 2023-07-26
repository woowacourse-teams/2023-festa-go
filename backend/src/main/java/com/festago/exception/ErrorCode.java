package com.festago.exception;

public enum ErrorCode {
    // 400
    NOT_MEMBER_TICKET_OWNER("해당 예매 티켓의 주인이 아닙니다."),
    NOT_ENTRY_TIME("입장 가능한 시간이 아닙니다."),
    EXPIRED_ENTRY_CODE("만료된 입장 코드입니다."),
    INVALID_ENTRY_CODE("올바르지 않은 입장코드입니다."),

    // 404
    MEMBER_TICKET_NOT_FOUND("존재하지 않은 멤버 티켓입니다."),

    // 500
    INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
    INVALID_ENTRY_CODE_PERIOD("올바르지 않은 입장코드 유효기간입니다."),
    INVALID_ENTRY_CODE_EXPIRATION_TIME("올바르지 않은 입장코드 만료 일자입니다."),
    INVALID_ENTRY_STATE_INDEX("올바르지 않은 입장상태 인덱스입니다."),
    INVALID_ENTRY_CODE_PAYLOAD("유효하지 않은 payload 입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
