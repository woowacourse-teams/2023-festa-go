package com.festago.support;

import com.festago.auth.domain.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WithMockAuth {

    long id() default 1;

    Role role() default Role.MEMBER;
}
