package com.festago.auth.annotation;

import com.festago.auth.domain.Role;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SecurityRequirement(name = "bearerAuth")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Authorization(role = Role.MEMBER)
public @interface MemberAuth {

}
