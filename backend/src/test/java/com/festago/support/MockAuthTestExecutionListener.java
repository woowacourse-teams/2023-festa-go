package com.festago.support;

import com.festago.auth.AuthenticateContext;
import com.festago.auth.domain.Role;
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
            authenticateContext.setAuthenticate(withMockAuth.id(), withMockAuth.role());
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        AuthenticateContext authenticateContext = applicationContext.getBean(AuthenticateContext.class);
        authenticateContext.setAuthenticate(null, Role.ANONYMOUS);
    }
}
