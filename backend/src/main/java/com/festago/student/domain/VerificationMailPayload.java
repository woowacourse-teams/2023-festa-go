package com.festago.student.domain;

import com.festago.common.exception.ValidException;
import org.springframework.util.StringUtils;

public class VerificationMailPayload {

    private final VerificationCode code;
    private final String username;
    private final String domain;

    public VerificationMailPayload(VerificationCode code, String username, String domain) {
        validate(code, username, domain);
        this.code = code;
        this.username = username;
        this.domain = domain;
    }

    private void validate(VerificationCode code, String username, String domain) {
        if (code == null) {
            throw new IllegalArgumentException("code는 null이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(username)) {
            throw new ValidException("username은 null 또는 공백이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(domain)) {
            throw new IllegalArgumentException("domain은 null 또는 공백이 될 수 없습니다.");
        }
    }

    public String getCode() {
        return code.getValue();
    }

    public String getUsername() {
        return username;
    }

    public String getDomain() {
        return domain;
    }
}
