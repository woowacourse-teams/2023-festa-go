package com.festago.common.filter;

import com.festago.common.aop.LogRequestBody;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * ApplicationReadyEvent를 통해 Lazy하게 UriPatternMatcher의 패턴을 추가하는 클래스 <br/>
 */
@Component
@RequiredArgsConstructor
public class UriPatternInitializer {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final UriPatternMatcher uriPatternMatcher;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        for (var entry : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            if (handlerMethod.hasMethodAnnotation(LogRequestBody.class)) {
                Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
                Set<String> directPaths = requestMappingInfo.getDirectPaths();
                uriPatternMatcher.addPattern(methods, directPaths);
            }
        }
    }
}
