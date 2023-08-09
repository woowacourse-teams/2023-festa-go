package com.festago.auth.dto;

import com.festago.auth.domain.SocialType;

public record LoginRequest(SocialType socialType, String accessToken) {

}
