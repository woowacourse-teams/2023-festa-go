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
        return new AdminLoginCommand(username, password);
    }
}
