package com.festago.admin.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.util.Validator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UNIQUE_USERNAME",
            columnNames = {"username"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {

    private static final int MIN_USERNAME_LENGTH = 4;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
    private String username;

    @NotNull
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    private String password;

    public Admin(String username, String password) {
        this(null, username, password);
    }

    public Admin(Long id, String username, String password) {
        validate(username, password);
        this.id = id;
        this.username = username;
        this.password = password;
    }

    private void validate(String username, String password) {
        validateUsername(username);
        validatePassword(password);
    }

    private void validateUsername(String username) {
        Validator.notNull(username, "username은 null이 될 수 없습니다.");
        Validator.minLength(username, MIN_USERNAME_LENGTH,
            () -> "username의 길이는 %d글자 미만일 수 없습니다.".formatted(MIN_USERNAME_LENGTH));
        Validator.maxLength(username, MAX_USERNAME_LENGTH,
            () -> "username의 길이는 %d글자를 초과할 수 없습니다.".formatted(MAX_USERNAME_LENGTH));
    }

    private void validatePassword(String password) {
        Validator.notNull(password, "password는 null 값이 될 수 없습니다.");
        Validator.minLength(password, MIN_PASSWORD_LENGTH,
            () -> "password의 길이는 %d글자 미만일 수 없습니다.".formatted(MIN_PASSWORD_LENGTH));
        Validator.maxLength(password, MAX_PASSWORD_LENGTH,
            () -> "password의 길이는 %d글자를 초과할 수 없습니다.".formatted(MAX_PASSWORD_LENGTH));
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
