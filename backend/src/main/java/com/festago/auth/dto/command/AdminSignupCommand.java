package com.festago.auth.dto.command;

public record AdminSignupCommand(
    String username,
    String password
) {

}
