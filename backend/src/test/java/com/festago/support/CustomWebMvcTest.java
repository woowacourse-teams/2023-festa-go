package com.festago.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@WebMvcTest
@Import(TestAuthConfig.class)
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = MockAuthTestExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface CustomWebMvcTest {

    @AliasFor("controllers")
    Class<?>[] value() default {};

    @AliasFor("value")
    Class<?>[] controllers() default {};
}
