package com.festago.student.domain;

import com.festago.common.exception.UnexpectedException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode {

    public static final int LENGTH = 6;
    private static final Pattern POSITIVE_REGEX = Pattern.compile("^\\d+$");
    @Column(name = "code")
    private String value;

    public VerificationCode(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        validateBlank(value);
        validateLength(value);
        validatePositive(value);
    }

    private void validateBlank(String value) {
        if (!StringUtils.hasText(value)) {
            throw new UnexpectedException("VerificationCode는 null 또는 공백이 될 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() != LENGTH) {
            throw new UnexpectedException("VerificationCode의 길이는 %d 이어야 합니다.".formatted(LENGTH));
        }
    }

    private void validatePositive(String value) {
        if (!POSITIVE_REGEX.matcher(value).matches()) {
            throw new UnexpectedException("VerificationCode는 0~9의 양수 형식이어야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
