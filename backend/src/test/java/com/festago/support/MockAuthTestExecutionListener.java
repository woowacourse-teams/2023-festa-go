package com.festago.support;

import com.festago.auth.domain.AuthenticateContext;
import com.festago.auth.domain.authentication.AdminAuthentication;
import com.festago.auth.domain.authentication.AnonymousAuthentication;
import com.festago.auth.domain.authentication.Authentication;
import com.festago.auth.domain.authentication.MemberAuthentication;
import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class MockAuthTestExecutionListener implements TestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        Method testMethod = testContext.getTestMethod();
        if (testMethod.isAnnotationPresent(WithMockAuth.class)) {
            WithMockAuth withMockAuth = testMethod.getDeclaredAnnotation(WithMockAuth.class);
            ApplicationContext applicationContext = testContext.getApplicationContext();
            AuthenticateContext authenticateContext = applicationContext.getBean(AuthenticateContext.class);
            long id = withMockAuth.id();
            Authentication authentication = switch (withMockAuth.role()) {
                case ANONYMOUS -> AnonymousAuthentication.getInstance();
                case MEMBER -> new MemberAuthentication(id);
                case ADMIN -> new AdminAuthentication(id);
            };
            authenticateContext.setAuthentication(authentication);
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        AuthenticateContext authenticateContext = applicationContext.getBean(AuthenticateContext.class);
        authenticateContext.setAuthentication(AnonymousAuthentication.getInstance());
    }
}
