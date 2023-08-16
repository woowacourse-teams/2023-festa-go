package com.festago.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;

public class VerificationMailPayload {

    private VerificationCode code;
    private String username;
    private String domain;

    public VerificationMailPayload(VerificationCode code, String username, String domain) {
        validate(code, username, domain);
        this.code = code;
        this.username = username;
        this.domain = domain;
    }

    private void validate(VerificationCode code, String username, String domain) {
        if (code == null || username == null || domain == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
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
