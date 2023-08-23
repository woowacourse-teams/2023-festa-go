package com.festago.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import com.festago.presentation.ErrorLogger;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.event.Level;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@Aspect
public class LogRequestBodyAspect {

    private static final long MAX_CONTENT_LENGTH = 1024;
    private static final String LOG_FORMAT = "[REQUEST BODY]\n{}";

    private final ErrorLogger errorLogger;
    private final ObjectMapper objectMapper;

    public LogRequestBodyAspect(ErrorLogger errorLogger, ObjectMapper objectMapper) {
        this.errorLogger = errorLogger;
        this.objectMapper = objectMapper;
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
        errorLogger.get(level)
            .log(LOG_FORMAT, getRequestPayload(request));
    }

    private String getRequestPayload(HttpServletRequest request) {
        try {
            ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
            return objectMapper.readTree(cachedRequest.getContentAsByteArray()).toPrettyString();
        } catch (IOException | ClassCastException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
