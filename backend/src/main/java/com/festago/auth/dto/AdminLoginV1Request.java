package com.festago.auth.dto;

import com.festago.auth.dto.command.AdminLoginCommand;
import jakarta.validation.constraints.NotBlank;

public record AdminLoginV1Request(
    @NotBlank
    String username,
    @NotBlank
    String password
) {

    public AdminLoginCommand toCommand() {
        return AdminLoginCommand.builder()
            .username(username)
            .password(password)
            .build();
    }
}
