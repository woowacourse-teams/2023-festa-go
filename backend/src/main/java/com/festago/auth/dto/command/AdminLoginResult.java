package com.festago.auth.dto.command;

import com.festago.auth.domain.AuthType;

public record AdminLoginResult(
    String username,
    AuthType authType,
    String accessToken
) {

}
