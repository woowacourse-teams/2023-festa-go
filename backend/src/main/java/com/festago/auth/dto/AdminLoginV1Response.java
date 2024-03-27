package com.festago.auth.dto;

import com.festago.auth.domain.AuthType;

public record AdminLoginV1Response(
    String username,
    AuthType authType
) {

}
