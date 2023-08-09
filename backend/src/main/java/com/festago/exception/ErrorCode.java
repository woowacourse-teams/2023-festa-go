package com.festago.exception;

public enum ErrorCode {
    // 400
    NOT_MEMBER_TICKET_OWNER("해당 예매 티켓의 주인이 아닙니다."),
    NOT_ENTRY_TIME("입장 가능한 시간이 아닙니다."),
    EXPIRED_ENTRY_CODE("만료된 입장 코드입니다."),
    INVALID_ENTRY_CODE("올바르지 않은 입장코드입니다."),
    INVALID_TICKET_OPEN_TIME("티켓은 공연 시작 전에 오픈되어야 합니다."),
    INVALID_STAGE_START_TIME("공연은 축제 기간 중에만 진행될 수 있습니다."),
    INVALID_MIN_TICKET_AMOUNT("티켓은 적어도 한장 이상 발급해야합니다."),
    LATE_TICKET_ENTRY_TIME("입장 시간은 공연 시간보다 빨라야합니다."),
    EARLY_TICKET_ENTRY_TIME("입장 시간은 공연 시작 12시간 이내여야 합니다."),
    EARLY_TICKET_ENTRY_THAN_OPEN("입장 시간은 티켓 오픈 시간 이후여야합니다."),
    TICKET_SOLD_OUT("매진된 티켓입니다."),
    INVALID_FESTIVAL_START_DATE("축제 시작 일자는 과거일 수 없습니다."),
    INVALID_FESTIVAL_DURATION("축제 시작 일자는 종료일자 이전이어야합니다."),
    INVALID_TICKET_CREATE_TIME("티켓 예매 시작 후 새롭게 티켓을 발급할 수 없습니다."),
    OAUTH2_INVALID_CODE("잘못된 인가 코드 입니다."),
    OAUTH2_NOT_SUPPORTED_SOCIAL_TYPE("해당 OAuth2 제공자는 지원되지 않습니다."),
    TICKET_RESERVE_TIMEOUT("티켓의 예매 시간은 공연 시작 시간보다 빨라야 합니다."),

    // 401
    EXPIRED_AUTH_TOKEN("만료된 로그인 토큰입니다."),
    INVALID_AUTH_TOKEN("올바르지 않은 로그인 토큰입니다."),
    NOT_BEARER_TOKEN_TYPE("Bearer 타입의 토큰이 아닙니다."),
    NEED_AUTH_TOKEN("로그인이 필요한 서비스입니다."),

    // 404
    MEMBER_TICKET_NOT_FOUND("존재하지 않은 멤버 티켓입니다."),
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다."),
    STAGE_NOT_FOUND("존재하지 않은 공연입니다."),
    FESTIVAL_NOT_FOUND("존재하지 않는 축제입니다."),
    TICKET_NOT_FOUND("존재하지 않는 티켓입니다."),

    // 500
    INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
    INVALID_ENTRY_CODE_PERIOD("올바르지 않은 입장코드 유효기간입니다."),
    INVALID_ENTRY_CODE_EXPIRATION_TIME("올바르지 않은 입장코드 만료 일자입니다."),
    INVALID_ENTRY_STATE_INDEX("올바르지 않은 입장상태 인덱스입니다."),
    INVALID_ENTRY_CODE_PAYLOAD("유효하지 않은 입장코드 payload 입니다."),
    INVALID_AUTH_TOKEN_PAYLOAD("유효하지 않은 로그인 토큰 payload 입니다."),
    DUPLICATE_SOCIAL_TYPE("중복된 OAuth2 제공자 입니다."),
    OAUTH2_PROVIDER_NOT_RESPONSE("OAuth2 제공자 서버에 문제가 발생했습니다."),
    OAUTH2_INVALID_REQUEST("OAuth2 제공자 서버에 잘못된 요청이 발생했습니다."),
    INVALID_ENTRY_CODE_OFFSET("올바르지 않은 입장코드 오프셋입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
