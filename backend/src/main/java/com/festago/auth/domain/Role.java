package com.festago.auth.domain;

import com.festago.auth.annotation.Admin;
import com.festago.auth.annotation.Anonymous;
import com.festago.auth.annotation.Member;
import com.festago.auth.annotation.Staff;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import java.lang.annotation.Annotation;

public enum Role {
    ANONYMOUS(Anonymous.class),
    MEMBER(Member.class),
    ADMIN(Admin.class),
    STAFF(Staff.class),
    ;

    private final Class<? extends Annotation> annotation;

    Role(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public static Role from(String role) {
        try {
            return valueOf(role);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new InternalServerException(ErrorCode.INVALID_ROLE_NAME);
        }
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
