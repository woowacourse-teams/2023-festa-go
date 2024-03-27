package com.festago.auth.dto.command;

public record AdminLoginCommand(
    String username,
    String password
) {

}
