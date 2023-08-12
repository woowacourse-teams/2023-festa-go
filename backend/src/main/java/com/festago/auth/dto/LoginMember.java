package com.festago.auth.dto;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public record LoginMember(
    Long memberId
) {

}
