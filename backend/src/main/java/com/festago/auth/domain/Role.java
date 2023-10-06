package com.festago.auth.domain;

import com.festago.auth.annotation.Admin;
import com.festago.auth.annotation.Anonymous;
import com.festago.auth.annotation.Member;
import java.lang.annotation.Annotation;

public enum Role {
    ANONYMOUS(Anonymous.class),
    MEMBER(Member.class),
    ADMIN(Admin.class),
    ;

    private final Class<? extends Annotation> annotation;

    Role(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public static Role from(String role) {
        try {
            return valueOf(role);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("해당하는 Role이 없습니다.");
        }
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
