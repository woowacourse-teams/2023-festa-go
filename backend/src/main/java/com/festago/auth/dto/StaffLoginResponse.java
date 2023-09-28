package com.festago.auth.dto;

public record StaffLoginResponse(
    Long festivalId,
    String accessToken
) {

}
