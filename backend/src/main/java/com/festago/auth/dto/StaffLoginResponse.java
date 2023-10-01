package com.festago.auth.dto;

public record StaffLoginResponse(
    Long staffId,
    String accessToken
) {

}
