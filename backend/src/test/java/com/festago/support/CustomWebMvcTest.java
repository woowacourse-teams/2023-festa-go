package com.festago.support;

import com.festago.config.ErrorLoggerConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@WebMvcTest
@Import({TestAuthConfig.class, ErrorLoggerConfig.class, MockAllServiceConfig.class})
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = {MockAuthTestExecutionListener.class,
    ResetMockTestExecutionListener.class}, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface CustomWebMvcTest {

    Class<?>[] value() default {};
}
