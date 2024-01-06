package com.festago.support;

import java.util.ArrayList;
import java.util.List;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class ResetMockTestExecutionListener implements TestExecutionListener {

    private static long applicationContextStartupDate;
    private static final List<Object> mockCache = new ArrayList<>();

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        if (mockCache.isEmpty() || isNewContext(applicationContext)) {
            mockCache.clear();
            applicationContextStartupDate = applicationContext.getStartupDate();
            initMocks(applicationContext);
        }
        mockCache.forEach(Mockito::reset);
    }

    private boolean isNewContext(ApplicationContext applicationContext) {
        return applicationContext.getStartupDate() != applicationContextStartupDate;
    }

    private void initMocks(ApplicationContext applicationContext) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            MockingDetails mockingDetails = Mockito.mockingDetails(bean);
            if (mockingDetails.isMock()) {
                mockCache.add(bean);
            }
        }
    }
}
