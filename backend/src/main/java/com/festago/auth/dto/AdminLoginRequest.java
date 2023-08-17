package com.festago.auth.dto;

public record AdminLoginRequest(
    String username,
    String password
) {

}
