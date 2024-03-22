package com.festago.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@Component
@Aspect
public class LogRequestBodyAspect {

    private static final long MAX_CONTENT_LENGTH = 1024;
    private static final String LOG_FORMAT = "\n[REQUEST BODY]\n{}";

    private final Map<Level, BiConsumer<String, String>> loggerMap = new EnumMap<>(Level.class);
    private final ObjectMapper objectMapper;
    private final Logger errorLogger;

    public LogRequestBodyAspect(ObjectMapper objectMapper, Logger errorLogger) {
        this.objectMapper = objectMapper;
        this.errorLogger = errorLogger;
        loggerMap.put(Level.INFO, this.errorLogger::info);
        loggerMap.put(Level.WARN, this.errorLogger::warn);
        loggerMap.put(Level.ERROR, this.errorLogger::error);
    }

    @Around("@annotation(LogRequestBody)")
    public Object handleAll(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LogRequestBody annotation = method.getAnnotation(LogRequestBody.class);
        Level level = annotation.level();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null || !errorLogger.isEnabledForLevel(level)) {
            return pjp.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        if (validateRequest(request)) {
            return pjp.proceed();
        }

        if (annotation.exceptionOnly()) {
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                log(level, request);
                throw e;
            }
        }

        log(level, request);
        return pjp.proceed();
    }

    private boolean validateRequest(HttpServletRequest request) {
        return !Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)
            || request.getContentLengthLong() > MAX_CONTENT_LENGTH;
    }

    private void log(Level level, HttpServletRequest request) {
        loggerMap.getOrDefault(level, (ignore1, ignore2) -> {
            })
            .accept(LOG_FORMAT, getRequestPayload(request));
    }

    private String getRequestPayload(HttpServletRequest request) {
        try {
            ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
            return objectMapper.readTree(cachedRequest.getContentAsByteArray()).toPrettyString();
        } catch (IOException e) {
            log.warn("ObjectMapper에서 직렬화 중에 문제가 발생했습니다.", e);
        } catch (ClassCastException e) {
            log.warn("HttpServletRequest 객체를 ContentCachingRequestWrapper 타입으로 형변환 하는 중 문제가 발생했습니다.", e);
        }
        return "[ObjectMapper에서 직렬화 중에 문제가 발생했습니다.]";
    }
}
