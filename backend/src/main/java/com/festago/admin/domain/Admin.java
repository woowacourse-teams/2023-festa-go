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
import java.util.Objects;
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

    public static final String ROOT_ADMIN_NAME = "admin";
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

    public static Admin createRootAdmin(String password) {
        return new Admin(ROOT_ADMIN_NAME, password);
    }

    private void validate(String username, String password) {
        validateUsername(username);
        validatePassword(password);
    }

    private void validateUsername(String username) {
        String fieldName = "username";
        Validator.notBlank(username, fieldName);
        Validator.minLength(username, MIN_USERNAME_LENGTH, fieldName);
        Validator.maxLength(username, MAX_USERNAME_LENGTH, fieldName);
    }

    private void validatePassword(String password) {
        String fieldName = "password";
        Validator.notBlank(password, fieldName);
        Validator.minLength(password, MIN_PASSWORD_LENGTH, fieldName);
        Validator.maxLength(password, MAX_PASSWORD_LENGTH, fieldName);
    }

    public boolean isRootAdmin() {
        return Objects.equals(username, ROOT_ADMIN_NAME);
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
