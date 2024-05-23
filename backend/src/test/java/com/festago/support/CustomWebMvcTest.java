package com.festago.support;

import com.festago.common.aop.ValidPageableAspect;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@EnableAspectJAutoProxy
@WebMvcTest
@Import({TestAuthConfig.class, MockAllServiceBeanFactoryPostProcessor.class, ValidPageableAspect.class})
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = {MockAuthTestExecutionListener.class,
    ResetMockTestExecutionListener.class}, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface CustomWebMvcTest {

    Class<?>[] value() default {};
}
