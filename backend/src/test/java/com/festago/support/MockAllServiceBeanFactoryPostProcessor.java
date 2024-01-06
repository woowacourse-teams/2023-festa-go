package com.festago.support;

import static org.mockito.Mockito.mock;

import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Service;

public class MockAllServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ClassFilter classFilter = ClassFilter.of(clazz -> clazz.isAnnotationPresent(Service.class));
        ReflectionUtils.findAllClassesInPackage("com.festago", classFilter)
            .forEach(clazz -> {
                BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
                registry.registerBeanDefinition(clazz.getSimpleName(), new RootBeanDefinition(clazz));
                beanFactory.registerSingleton(clazz.getSimpleName(), mock(clazz));
            });
    }
}
