package com.festago.support;

import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class ResetMockTestExecutionListener implements TestExecutionListener {

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            MockingDetails mockingDetails = Mockito.mockingDetails(bean);
            if (mockingDetails.isMock()) {
                Mockito.reset(bean);
            }
        }
    }
}
